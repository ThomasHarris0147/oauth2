import os
import boto3
import pika
from pathlib import Path
import os
import shutil

def rewritePlaylist(playlistLoc, file_prefixes):
    cdn = "https://d2tsi3g28pn9oo.cloudfront.net"

    # READ
    playlist = open(playlistLoc,"rt")
    data = playlist.read()
    # MODIFY
    data = data.replace(file_prefixes, cdn+"/"+file_prefixes+"/"+file_prefixes)
    playlist.close()

    #WRITE
    playlist = open(playlistLoc,"wt")
    playlist.write(data)
    playlist.close()

s3_bucket = 'nettube-vod'

# login credentials should be in your ~/.aws/credentials
# extra settings in ~/.aws/config
s3_connect = boto3.client('s3')

connection = pika.BlockingConnection(
    pika.ConnectionParameters(host='localhost',heartbeat=0))
channel = connection.channel()

channel.queue_declare(queue='task_upload_queue', durable=True)
print(' [*] Waiting for messages. To exit press CTRL+C')

def callback(ch, method, properties, body):
    message = body.decode()
    print(" [x] Received %r" % message)
    messageList = message.split(' ')
    folder = messageList[0] # directory with all chunks
    resolution = messageList[1]
    key_name = Path(os.path.dirname(folder)).stem + '/'
    if resolution == "-1":
        key_name = os.path.basename(os.path.dirname(folder)) + '/'
        s3_connect.put_object(Bucket=s3_bucket, Key=key_name)
        file_key_name = key_name + os.path.basename(folder)
        print(key_name)
        print(file_key_name)
        upload = s3_connect.upload_file(folder, s3_bucket, file_key_name)
        #os.remove(folder)
        print(" [x] Done")
        ch.basic_ack(delivery_tag=method.delivery_tag)
        return
    bucket = s3_connect.put_object(Bucket=s3_bucket, Key=key_name)
    rewritePlaylist(folder+"/playlist.m3u8",Path(folder).stem)
    os.rename(folder+"/playlist.m3u8", folder+"/playlist"+resolution+".m3u8")
    # upload File to S3
    for filename in os.listdir(folder):
        file_key_name = key_name + filename
        local_name = folder + '/' + filename
        upload = s3_connect.upload_file(local_name, s3_bucket, file_key_name)
        print(file_key_name+" uploaded")    
    shutil.rmtree(folder)
    print(" [x] Done")
    ch.basic_ack(delivery_tag=method.delivery_tag)

channel.basic_qos(prefetch_count=1)
channel.basic_consume(queue='task_upload_queue', on_message_callback=callback)

channel.start_consuming()