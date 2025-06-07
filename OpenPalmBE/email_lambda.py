import json

def lambda_handler(event, context):
    print("Received event:")
    print(json.dumps(event, indent=2))  # Nicely formatted log output

    return {
        'statusCode': 200,
        'body': json.dumps({'message': 'Received successfully'})
    }
