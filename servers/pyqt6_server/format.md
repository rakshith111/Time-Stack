# Init Expected Data
```json
{
    "challenge_code":"Key from qr",
    
}
```
# Once the server acknowledges the challenge code, it will send the following data
```json
{
    "challenge_code":"Key from qr",
    "challenge_code_accepted":True,
}

```
# If the challenge code is not accepted, the server will send the following data then close the connection
```json
{

    "challenge_code_accepted":False,
    "error":"Error message",
}
```
------
# General req
```json
{
    "challenge_code":"Key from qr",
    "endpoint":"ENDPOINT",
    **optional**
    { 
        "name":"NAME",
        "value":"VALUE"
        }
}

```

## Current endpoints
# ENDPOINT: "get"
