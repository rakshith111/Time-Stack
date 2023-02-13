import os
import subprocess
ui_path = os.path.join(os.path.dirname(__file__), 'ui_files')
ui_files= os.listdir(ui_path)
for ui_file in ui_files:
    ui_file_path = os.path.join(ui_path, ui_file)
    py_file_path = os.path.join(os.path.dirname(__file__), 'ui_modules', ui_file[:-3] + '.py')

    subprocess.call(['python', '-m', 'PyQt6.uic.pyuic', '-x', ui_file_path, '-o', py_file_path])
