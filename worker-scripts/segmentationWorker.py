import pika
from pathlib import Path
import subprocess
import os

# send task to uploading queue
def sendChunksForUploading(message):
    connection = pika.BlockingConnection(
        pika.ConnectionParameters(host='localhost'))
    channel = connection.channel()

    channel.queue_declare(queue='task_upload_queue', durable=True)

    channel.basic_publish(
        exchange='',
        routing_key='task_upload_queue',
        body=message,
        properties=pika.BasicProperties(
            delivery_mode=pika.spec.PERSISTENT_DELIVERY_MODE
        ))
    print(" [x] Sent %r" % message)
    connection.close()

connection = pika.BlockingConnection(
    pika.ConnectionParameters(host='localhost'))
channel = connection.channel()

channel.queue_declare(queue='task_vid_queue', durable=True)
print(' [*] Waiting for messages. To exit press CTRL+C')

def callback(ch, method, properties, body):
    message = body.decode()
    print(" [x] Received %r" % message)
    messageList = message.split(' ')
    videoPath = messageList[0]
    resolution = messageList[1]
    fileName = Path(videoPath).stem
    dirName = os.path.dirname(videoPath)
    newDir = dirName+"/"+fileName+"-chunks"
    if (os.path.isdir(newDir) == False):
        os.mkdir(newDir)
    cmd = [
        "ffmpeg",
        "-i", videoPath,
        "-c", "copy",
        "-map", "0",
        "-segment_time", "00:01:00",
        "-f", "segment",
        "-reset_timestamps", "1",
        "-segment_format","mpegts",
        "-segment_list",newDir+"/"+"playlist.m3u8",
        "-segment_list_type","m3u8",
        newDir+"/"+fileName+"-%d.ts"
    ]
    subprocess.run(cmd)
    uploadingMessage = newDir+" "+resolution
    sendChunksForUploading(uploadingMessage)
    print(" [x] Done")
    ch.basic_ack(delivery_tag=method.delivery_tag)

channel.basic_qos(prefetch_count=1)
channel.basic_consume(queue='task_vid_queue', on_message_callback=callback)

channel.start_consuming()