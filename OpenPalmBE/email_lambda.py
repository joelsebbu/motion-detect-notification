import boto3
import json
import os

# Read sender email from environment variable
SENDER_EMAIL = os.environ.get('SES_SENDER_EMAIL')
EMAIL_REGION = os.environ.get('SES_EMAIL_REGION')

# Set up SES client
ses = boto3.client('ses', region_name=EMAIL_REGION)


def lambda_handler(event, context):
    print("[LOG_EVENT] Raw event received:", json.dumps(event))

    try:
        body = json.loads(event.get('body', '{}'))
        email = body.get('email')

        print(f"[LOG_INPUT] Parsed email: {email}")

        if not email:
            print("[LOG_ERROR] Email field missing in request body")
            return {
                'statusCode': 400,
                'body': json.dumps({'error': 'Missing email in request'})
            }

        if not SENDER_EMAIL:
            print("[LOG_ERROR] SES_SENDER_EMAIL not set in environment variables")
            return {
                'statusCode': 500,
                'body': json.dumps({'error': 'SES_SENDER_EMAIL is not set'})
            }

        if email == SENDER_EMAIL:

            print(f"[LOG_ACTION] Sending email to {email}")
            ses.send_email(
                Source=SENDER_EMAIL,
                Destination={'ToAddresses': [email]},
                Message={
                    'Subject': {'Data': 'Hi from OpenPalmApp'},
                    'Body': {
                        'Text': {'Data': 'This is a test message from OpenPalmApp.'}
                    }
                }
            )
            print("[LOG_SUCCESS] Email sent via SES")
            return {
                'statusCode': 200,
                'body': json.dumps({'message': 'Email sent successfully'})
            }
        else:
            print(f"[LOG_INFO] Email not sent (sandbox restriction). To: {email}")
            return {
                'statusCode': 200,
                'body': json.dumps({
                    'message': 'Email Logging successfull (email not sent in sandbox mode)', 
                    "email_sent": False
                    }
                )
            }

    except Exception as e:
        print(f"[LOG_EXCEPTION] {str(e)}")
        return {
            'statusCode': 500,
            'body': json.dumps({'error': str(e)})
        }
