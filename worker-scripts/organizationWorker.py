import pika
from pathlib import Path
import subprocess
import os
import shutil

# Convert list into string
def listToString(s):
    str1 = "" 
    for ele in s:
        str1 += ele + "."
    return str1

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

# send task to vidqueue
def sendVideoToChunking(message):
    connection = pika.BlockingConnection(
        pika.ConnectionParameters(host='localhost'))
    channel = connection.channel()

    channel.queue_declare(queue='task_vid_queue', durable=True)

    channel.basic_publish(
        exchange='',
        routing_key='task_vid_queue',
        body=message,
        properties=pika.BasicProperties(
            delivery_mode=pika.spec.PERSISTENT_DELIVERY_MODE
        ))
    print(" [x] Sent %r" % message)
    connection.close()

# send video to compressing
def sendVideoToCompressing(message):
    connection = pika.BlockingConnection(
        pika.ConnectionParameters(host='localhost'))
    channel = connection.channel()
    
    channel.queue_declare(queue='task_com_queue', durable=True)

    channel.basic_publish(
        exchange='',
        routing_key='task_com_queue',
        body=message,
        properties=pika.BasicProperties(
            delivery_mode=pika.spec.PERSISTENT_DELIVERY_MODE
        ))
    print(" [x] Sent %r" % message)
    connection.close()

connection = pika.BlockingConnection(
    pika.ConnectionParameters(host='localhost'))
channel = connection.channel()

channel.queue_declare(queue='task_org_queue', durable=True)
print(' [*] Waiting for messages. To exit press CTRL+C')

def callback(ch, method, properties, body):
    resolutions_lst = []
    message = body.decode()
    print(" [x] Received %r" % message)
    messageList = message.split(' ')
    videoPath = messageList[0]
    if (not os.path.isfile(videoPath)):
        print(" [!] Path provided is not correct!")
    else:
        fileName = Path(videoPath).stem
        fileDots = fileName.split(".")
        resolution = fileDots[-1]
        folderPrefixList = fileDots[:-1]
        folderPrefix = listToString(folderPrefixList)
        dirName = os.path.dirname(videoPath)
        if (resolution == "360p"):
            newDir = dirName+"/"+folderPrefix+"360p"
            if (os.path.isdir(newDir) == False):
                os.mkdir(newDir)
            shutil.copy(videoPath,newDir+"\\"+fileName+".mp4")
            sendVideoToChunking(newDir+"\\"+fileName+".mp4 360p")
            resolutions_lst.append(360)
        elif (resolution == "1080p"):
            newDir = dirName+"/"+folderPrefix+"1080p"
            if (os.path.isdir(newDir) == False):
                os.mkdir(newDir)
            shutil.copy(videoPath,newDir+"\\"+fileName+".mp4")
            sendVideoToChunking(newDir+"\\"+fileName+".mp4 1080p")
            resolutions_lst.append(1080)
        elif (resolution == "720p"):
            newDir = dirName+"/"+folderPrefix+"720p"
            if (os.path.isdir(newDir) == False):
                os.mkdir(newDir)
            shutil.copy(videoPath,newDir+"\\"+fileName+".mp4")
            sendVideoToChunking(newDir+"\\"+fileName+".mp4 720p")
            resolutions_lst.append(720)
        else:
            raise Exception("file resolution error")
        
        if (resolution != "360p"):
            newDir = dirName+"/"+folderPrefix+"360p"
            if (os.path.isdir(newDir) == False):
                os.mkdir(newDir)
            # 360 compression
            sendVideoToCompressing(videoPath+" 360")
            resolutions_lst.append(360)
        if (resolution != "720p" and resolution != "360p"):
            newDir = dirName+"/"+folderPrefix+"720p"
            if (os.path.isdir(newDir) == False):
                os.mkdir(newDir)
            sendVideoToCompressing(videoPath+" 720")
            resolutions_lst.append(720)
        
        # making master.m3u8
        Lines = ["#EXTM3U\n","#EXT-X-VERSION:3\n"]
        os.mkdir(dirName+"/"+folderPrefix[:-1])
        file = open(dirName+"/"+folderPrefix[:-1]+"/master.m3u8", "a+")
        file.writelines(Lines)
        for res in resolutions_lst:
            lines = ["#EXT-X-STREAM-INF:BANDWIDTH=76280,AVERAGE-BANDWIDTH=69600\n", "https://d2tsi3g28pn9oo.cloudfront.net/"+folderPrefix[:-1]+"/playlist360p.m3u8\n"]
            if (res == 720):
                lines = ["#EXT-X-STREAM-INF:BANDWIDTH=223280,AVERAGE-BANDWIDTH=209600\n", "https://d2tsi3g28pn9oo.cloudfront.net/"+folderPrefix[:-1]+"/playlist720p.m3u8\n"]
            elif (res == 1080):
                lines = ["#EXT-X-STREAM-INF:BANDWIDTH=433280,AVERAGE-BANDWIDTH=409600\n", "https://d2tsi3g28pn9oo.cloudfront.net/"+folderPrefix[:-1]+"/playlist1080p.m3u8\n"]
            file.writelines(lines)
        sendChunksForUploading(dirName+"/"+folderPrefix[:-1]+"/master.m3u8 -1")
        

    print(" [x] Done")
    ch.basic_ack(delivery_tag=method.delivery_tag)

channel.basic_qos(prefetch_count=1)
channel.basic_consume(queue='task_org_queue', on_message_callback=callback)

channel.start_consuming()