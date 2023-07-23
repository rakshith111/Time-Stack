import random
import re

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
