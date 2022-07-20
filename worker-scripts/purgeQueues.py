import pika
connection = pika.BlockingConnection(
    pika.ConnectionParameters(host='localhost'))
channel = connection.channel()

channel.queue_purge("task_vid_queue")
channel.queue_purge("task_com_queue")
channel.queue_purge("task_org_queue")
channel.queue_purge("task_upload_queue")