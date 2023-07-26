import logging
import logging.config
import pathlib
import sys
# Go up one level to the src folder
if hasattr(sys, '_MEIPASS'):
        # Bundled with PyInstaller
        BASE_DIR_RESC = sys._MEIPASS
else:
        # Normal Python development mode
        BASE_DIR_RESC = pathlib.Path(__file__).parent.parent.parent.absolute()
log_file_path = pathlib.Path(BASE_DIR_RESC, 'src', 'configs', 'logging.ini')
logging.config.fileConfig(log_file_path)
logger=logging.getLogger('QTApp')
BASE_DIR = BASE_DIR_RESC
