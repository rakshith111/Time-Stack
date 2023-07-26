import sqlite3
import pathlib
import datetime
from libs._base_logger import logger
from libs.color import Color
from PyQt6.QtCore import QStandardPaths

MONTH_YEAR = datetime.datetime.now().strftime("%B_%Y")
BASE_SAVE_PATH = pathlib.Path(QStandardPaths.writableLocation(QStandardPaths.StandardLocation.AppLocalDataLocation))
SAVE_FILE = BASE_SAVE_PATH / 'TIME_STACK' / 'user_data' / 'db' / f'database_stack.db'


def check_db() -> None:
    '''
    This function checks if the database file exists. If it does not exist, it creates a new database file.
    # 0 - activity_id , 1 - activity_name , 2 - activity_mode ,
    # 3 - activity_start , 4 - activity_stop , 5 - activity_completed ,
    # 6 - activity_original_size , 7 - activity_latest_delta , 8 - position
    '''
    logger.info(f'{Color.GREEN}Checking if {SAVE_FILE} exists{Color.ENDC}')
    if not SAVE_FILE.exists():
        logger.info(f'{Color.RED}File {SAVE_FILE} does not exist{Color.ENDC}')
        logger.info(f'{Color.GREEN}Creating file {SAVE_FILE}{Color.ENDC}')

        SAVE_FILE.parent.mkdir(parents=True, exist_ok=True)
        connection, cursor = connect_db()
        cursor.execute(f'''CREATE TABLE {MONTH_YEAR}_stack
                        ( activity_id INTEGER PRIMARY KEY , 
                        activity_name TEXT NOT NULL,
                        activity_mode TEXT NOT NULL,
                        activity_start DATETIME NOT NULL,
                        activity_stop DATETIME NOT NULL,
                        activity_completed INT NOT NULL,
                        activity_original_size INT NOT NULL,
                        activity_latest_delta INT NOT NULL,
                        position INT NOT NULL );''')
        disconnect_db(connection, cursor)
    else:
        backup_db()
        logger.info(f'{Color.GREEN}File {SAVE_FILE} exists{Color.ENDC}')


def backup_db() -> None:
    current_date = datetime.datetime.now().strftime('%Y-%m-%d')
    # is it 5th day of the month ?
    if current_date.split('-')[2] == '05':  # Check for the 5th day of the month
        logger.info(f'{Color.GREEN}5th day of the month{Color.ENDC}')
        # Create a new database file for the backup
        # Get only month and year from the current date
        current_date_month_year = datetime.datetime.now().strftime('%Y_%m')
        backup_file_name = f'database_stack_{current_date_month_year}.db'
        backup_SAVE_FILE = BASE_SAVE_PATH / 'TIME_STACK' / 'user_data' / 'db' / backup_file_name

        if not backup_SAVE_FILE.exists():
            connection, cursor = connect_db()

            # Backup each table to the new database file
            cursor.execute("SELECT name FROM sqlite_master WHERE type='table';")
            tables = cursor.fetchall()

            for table in tables:
                table_name = table[0]
                backup_table(connection, cursor, table_name, backup_file_name)
            disconnect_db(connection, cursor)
            logger.info(f'{Color.GREEN}Data backed up on {backup_SAVE_FILE}{Color.ENDC}')
        else:
            logger.info(f'{Color.RED}Data already backed up on {backup_SAVE_FILE}{Color.ENDC}')
    else:
        logger.info(f'{Color.GREEN}Not 5th day of the month{Color.ENDC}')


def backup_table(connection: sqlite3.Connection, cursor: sqlite3.Cursor, table_name: str, backup_file_name: str):
    cursor.execute(f"SELECT activity_stop FROM {table_name}")
    dates = cursor.fetchall()

    if not dates:
        return

    last_month = datetime.datetime.strptime(dates[-1][0], "%Y-%m-%d %H:%M:%S")
    # Get only month and year from the last date
    last_month_year_month = last_month.strftime('%Y_%m')

    current_date = datetime.datetime.now().strftime('%Y-%m-%d')
    current_date_month_year = datetime.datetime.now().strftime('%Y_%m')

    # Compare the month and year to determine if backup is needed
    if last_month_year_month != current_date_month_year:
        # Create a new database file
        update_SAVE_FILE = BASE_SAVE_PATH / 'TIME_STACK' / 'user_data' / 'db' / f'database_stack_{last_month_year_month}.db'

        if not update_SAVE_FILE.exists():
            new_db = sqlite3.connect(update_SAVE_FILE)
            new_cursor = new_db.cursor()

            # Create a new table in the new database file
            new_cursor.execute(f'''CREATE TABLE {table_name}
                ( activity_id INTEGER PRIMARY KEY , 
                activity_name TEXT NOT NULL,
                activity_mode TEXT NOT NULL,
                activity_start DATETIME NOT NULL,
                activity_stop DATETIME NOT NULL,
                activity_completed INT NOT NULL,
                activity_original_size INT NOT NULL,
                activity_latest_delta INT NOT NULL,
                position INT NOT NULL );''')

            # Copy all the data from the old table to the new table
            cursor.execute(f"SELECT * FROM {table_name}")
            data = cursor.fetchall()

            for row in data:
                new_cursor.execute(f'''INSERT INTO {table_name} VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)''', row)

            # Delete all the data from the old table
            cursor.execute(f"DELETE FROM {table_name}")

            # Commit the changes
            new_db.commit()
            new_db.close()

            # Delete the old table from the old database file
            cursor.execute(f"DROP TABLE {table_name}")

            # Create a new table of new month and year in the old database file
            cursor.execute(f'''CREATE TABLE {MONTH_YEAR}_stack
                ( activity_id INTEGER PRIMARY KEY ,
                activity_name TEXT NOT NULL,
                activity_mode TEXT NOT NULL,
                activity_start DATETIME NOT NULL,
                activity_stop DATETIME NOT NULL,
                activity_completed INT NOT NULL,
                activity_original_size INT NOT NULL,
                activity_latest_delta INT NOT NULL,
                position INT NOT NULL );''')
            logger.info(f'{Color.GREEN}Created new table {MONTH_YEAR}_stack in {SAVE_FILE}{Color.ENDC}')
            # Commit the changes
            connection.commit()

        else:
            logger.info(f'{Color.RED}Backup file {update_SAVE_FILE} already exists{Color.ENDC}')


def connect_db() -> tuple:
    '''
    This function connects to the database and returns the connection and cursor objects.

    Returns:
        tuple: (connection, cursor)
    '''
    connection = sqlite3.connect(SAVE_FILE)
    cursor = connection.cursor()
    return connection, cursor


def disconnect_db(connection: sqlite3.Connection, cursor: sqlite3.Cursor) -> None:
    '''
    This function disconnects from the database.

    Args:
        connection (sqlite3.Connection): A connection object to the database.
        cursor (sqlite3.Cursor): A cursor object to the database.
    '''
    connection.commit()
    connection.close()

