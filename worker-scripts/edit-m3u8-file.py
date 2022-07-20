import datetime
import sys

from cryptography.hazmat.backends import default_backend
from cryptography.hazmat.primitives import hashes
from cryptography.hazmat.primitives import serialization
from cryptography.hazmat.primitives.asymmetric import padding
from botocore.signers import CloudFrontSigner

def rsa_signer(message):
    with open('~/mykeypair.pem', 'rb') as key_file:
        private_key = serialization.load_pem_private_key(
            key_file.read(),
            password=None,
            backend=default_backend()
        )
    return private_key.sign(message, padding.PKCS1v15(), hashes.SHA1())

key_id = "AKIAVIVPPG35FAZ2W7FK"
expire_date = datetime.datetime.now() + datetime.timedelta(days=1)

video = sys.argv[1]

url = 'http://d2949o5mkkp72v.cloudfront.net/'+video+"/playlist.m3u8"
cloudfront_signer = CloudFrontSigner(key_id, rsa_signer)
signed_url = cloudfront_signer.generate_presigned_url(
    url, date_less_than=expire_date)