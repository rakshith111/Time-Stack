import random
import re
import socket

def rng_gen()->int:
        '''
        This function generates a random number between 1 and 10000.

        Returns:
            int:A random number between 1 and 10000. 
        '''        
        rand_no = random.randint(1, 10000)
        return rand_no
    
def clean_input_name(name:str)->str:
        '''
        A Clean function to clean the name of the activity.

        Args:
            name (str): A string that is to be cleaned.

        Returns:
            str: A cleaned string.
        ''' 
        cleaned_name = name.strip()
        cleaned_name = re.sub(r'[^a-zA-Z0-9_]', '_', cleaned_name)
        # Limit the length of the name to 50 characters
        cleaned_name = cleaned_name[:50]
        return cleaned_name     

def get_local_ip() -> str:
    '''
    A function that returns the local IP address.
    It makes a connection to Google's DNS server and returns the local IP address.
    '''
    try:
        s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        s.connect(("8.8.8.8", 80))
        local_ip = s.getsockname()[0]
        s.close()
        return local_ip
    except:
        return None