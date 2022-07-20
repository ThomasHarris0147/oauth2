import pika
import sys

connection = pika.BlockingConnection(
    pika.ConnectionParameters(host='localhost'))
channel = connection.channel()

channel.queue_declare(queue='task_org_queue', durable=True)
channel.queue_declare(queue='task_com_queue', durable=True)
channel.queue_declare(queue='task_vid_queue', durable=True)

message = sys.argv[1]
channel.basic_publish(
    exchange='',
    routing_key='task_org_queue',
    body=message,
    properties=pika.BasicProperties(
        delivery_mode=pika.spec.PERSISTENT_DELIVERY_MODE
    ))
print(" [x] Sent %r" % message)
connection.close()