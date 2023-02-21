import os
import subprocess
ui_path = os.path.join(os.path.dirname(__file__), 'ui_files')

for ui_file in os.listdir(ui_path):
    if ui_file.endswith('.ui'):
        ui_file_path = os.path.join(ui_path, ui_file)
        py_file_path = os.path.join(os.path.dirname(
            __file__), 'ui_modules', ui_file[:-3] + '.py')
        subprocess.call(['python', '-m', 'PyQt6.uic.pyuic',
                        '-x', ui_file_path, '-o', py_file_path])
    elif ui_file.endswith('.py'):
        qrc_file_path = os.path.join(ui_path, ui_file)
        os.remove(qrc_file_path)
