import logging
import logging.config
from os import path
# Go up one level to the src folder
log_file_path = path.join(path.dirname(
        path.dirname(path.abspath(__file__))),'configs', 'logging.ini')
logging.config.fileConfig(log_file_path)
logger=logging.getLogger('QTApp')
BASE_DIR = path.join(path.dirname(
        path.dirname(path.abspath(__file__))))
