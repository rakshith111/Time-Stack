# -*- mode: python ; coding: utf-8 -*-


block_cipher = None


a = Analysis(
    ['src\\QTime_Stack_app.py'],
    pathex=[],
    binaries=[],
    datas=[ ('src\\configs\\logging.ini','src\\configs'),
            ('src\\ui_files\\icon\\hourglass_black.png','src\\ui_files\\icon'),
            ('src\\ui_files\\icon\\hourglass_white.png','src\\ui_files\\icon'),
            ('src\\ui_files\\icon\\moon.png','src\\ui_files\\icon'),
            ('src\\ui_files\\icon\\pause_black.png','src\\ui_files\\icon'),
            ('src\\ui_files\\icon\\pause_white.png','src\\ui_files\\icon'),
            ('src\\ui_files\\icon\\play_black.png','src\\ui_files\\icon'),
            ('src\\ui_files\\icon\\play_white.png','src\\ui_files\\icon'),
            ('src\\ui_files\\icon\\remove_black.png','src\\ui_files\\icon'),
            ('src\\ui_files\\icon\\remove_white.png','src\\ui_files\\icon'),
            ('src\\ui_files\\icon\\settings_black.png','src\\ui_files\\icon'),
            ('src\\ui_files\\icon\\settings_white.png','src\\ui_files\\icon'),
            ('src\\ui_files\\icon\\sun.png','src\\ui_files\\icon'),
            ('src\\ui_files\\icon\\time_stack_white_black.png','src\\ui_files\\icon'),
            ('src\\ui_files\\icon\\time_stack_white_small.png','src\\ui_files\\icon'),
            ('src\\ui_files\\icon\\to_tray_black.png','src\\ui_files\\icon'),
            ('src\\ui_files\\icon\\to_tray_white.png','src\\ui_files\\icon'),
            ('src\\ui_files\\icon\\window_icon_bow.png','src\\ui_files\\icon'),
            ('src\\ui_files\\icon\\window_icon_bow_s.png','src\\ui_files\\icon'),
            ('src\\ui_files\\icon\\window_icon_wob_s.png','src\\ui_files\\icon'),
            ('src\\ui_files\\icon\\active_notif_black.png','src\\ui_files\\icon'),
            ('src\\ui_files\\icon\\active_notif_white.png','src\\ui_files\\icon'),
            ('src\\ui_files\\icon\\add_black.png','src\\ui_files\\icon'),
            ('src\\ui_files\\icon\\add_white.png','src\\ui_files\\icon'),
            ('src\\ui_files\\icon\\horizontal_black.png','src\\ui_files\\icon'),
            ('src\\ui_files\\icon\\horizontal_white.png','src\\ui_files\\icon'),
            ('src\\ui_files\\icon\\inactive_notif_black.png','src\\ui_files\\icon'),
            ('src\\ui_files\\icon\\inactive_notif_white.png','src\\ui_files\\icon'),

            ('src\\ui_files\\Stack.qss','src\\ui_files'),

            # Sounds

            ('src\\ui_files\\sounds\\done-for-you.wav','src\\ui_files\\sounds'),
            ('src\\ui_files\\sounds\\good-news(Default_quat).wav','src\\ui_files\\sounds'),
            ('src\\ui_files\\sounds\\pristine.wav','src\\ui_files\\sounds'),
            ('src\\ui_files\\sounds\\smile.wav','src\\ui_files\\sounds'),
            ('src\\ui_files\\sounds\\so-proud-.wav','src\\ui_files\\sounds'),
            ('src\\ui_files\\sounds\\strong-minded(Default_end).wav','src\\ui_files\\sounds'),
            ('src\\ui_files\\sounds\\swiftly.wav','src\\ui_files\\sounds'),
            ('src\\ui_files\\sounds\\that-was-quick(Default_midway).wav','src\\ui_files\\sounds'),
            ('src\\ui_files\\sounds\\upset(Default_gen).wav','src\\ui_files\\sounds'),
    ],
    hiddenimports=[],
    hookspath=[],
    hooksconfig={},
    runtime_hooks=[],
    excludes=[],
    win_no_prefer_redirects=False,
    win_private_assemblies=False,
    cipher=block_cipher,
    noarchive=False,
)
pyz = PYZ(a.pure, a.zipped_data, cipher=block_cipher)

exe = EXE(
    pyz,
    a.scripts,
    [],
    exclude_binaries=True,
    name='QTime_Stack_app',
    debug=False,
    bootloader_ignore_signals=False,
    strip=False,
    upx=True,
    console=True,
    disable_windowed_traceback=False,
    argv_emulation=False,
    target_arch=None,
    codesign_identity=None,
    entitlements_file=None,
)
coll = COLLECT(
    exe,
    a.binaries,
    a.zipfiles,
    a.datas,
    strip=False,
    upx=True,
    upx_exclude=[],
    name='QTime_Stack_app',
)
