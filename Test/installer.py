from pathlib import Path
import os
import sys
import shutil
import PyQt6

os.chdir(Path(__file__).parent)


PYQT6_PATH = PyQt6.__path__[0]
bin_path = Path(os.path.join(PYQT6_PATH, 'Qt6', 'bin'))
ql_labs_path = Path(os.path.join(PYQT6_PATH, 'Qt6', 'qml'))

current_bin_path = Path(os.path.join('libs', 'bin'))

current_ql_labs_path = Path(os.path.join('libs', 'qml'))
shutil.copytree(current_bin_path, bin_path, dirs_exist_ok=True)
shutil.copytree(current_ql_labs_path, ql_labs_path, dirs_exist_ok=True)
