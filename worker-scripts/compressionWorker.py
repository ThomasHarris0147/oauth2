import pika
from pathlib import Path
import subprocess
import os

# Convert list into string
def listToString(s):
    str1 = "" 
    for ele in s:
        str1 += ele + "."
    return str1

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

connection = pika.BlockingConnection(
    pika.ConnectionParameters(host='localhost',heartbeat=0))
channel = connection.channel()

channel.queue_declare(queue='task_com_queue', durable=True)
print(' [*] Waiting for messages. To exit press CTRL+C')

def callback(ch, method, properties, body):
    message = body.decode()
    print(" [x] Received %r" % message)
    messageList = message.split(' ')
    videoPath = messageList[0]
    compressionSize = messageList[1]
    dirName = os.path.dirname(videoPath)

    fileName = Path(videoPath).stem
    fileDots = fileName.split(".")
    folderPrefixList = fileDots[:-1]
    folderPrefix = listToString(folderPrefixList)
    if (compressionSize == "720"):
        newDir = dirName+"/"+folderPrefix+"720p"
        cmd = [
            "ffmpeg",
            "-i", videoPath,
            "-c:v","libx264",
            "-s","ntsc",
            "-y",
            newDir+"/"+folderPrefix+"720p.mp4"
        ]
        try:
            subprocess.run(cmd)
        except Exception as e:
            print(e)
            print("likely file has successfully compiled however, continuing")
        sendVideoToChunking(newDir+"/"+folderPrefix+"720p.mp4 720p")
    if (compressionSize == "360"):
        newDir = dirName+"/"+folderPrefix+"360p"
        cmd = [
            "ffmpeg",
            "-i", videoPath,
            "-c:v","libx264",
            "-s","film",
            "-y",
            newDir+"/"+folderPrefix+"360p.mp4"
        ]
        try:
            subprocess.run(cmd)
        except Exception as e:
            print(e)
            print("likely file has successfully compiled however, continuing")
        sendVideoToChunking(newDir+"/"+folderPrefix+"360p.mp4 360p")
    print(" [x] Done")
    ch.basic_ack(delivery_tag=method.delivery_tag)

channel.basic_qos(prefetch_count=1)
channel.basic_consume(queue='task_com_queue', on_message_callback=callback)

channel.start_consuming()