# import the required libraries
from __future__ import print_function

import io
import shutil
import sys
import re
import os
from mimetypes import MimeTypes

from googleapiclient.discovery import build
from googleapiclient.http import MediaIoBaseDownload, MediaFileUpload
from httplib2 import Http
# Define the scopes
from oauth2client.service_account import ServiceAccountCredentials

SCOPES = ['https://www.googleapis.com/auth/drive']


class DriveAPI:
    def __init__(self):
        cur_path = os.path.dirname(os.path.realpath(__file__))
        self.creds = ServiceAccountCredentials.from_json_keyfile_name(cur_path + '/sa-tomtom-311823-c9f79ec5b3ed.json', SCOPES)
        http_auth = self.creds.authorize(Http())
        # Connect to the API service
        self.service = build('drive', 'v3', http=http_auth)

        self.files = self.GetFiles()

    def FileDownload(self, file, by_id):
        if by_id:
            file_id = file
            file_name = None
            for item in self.files:
                if item['id'] == file_id:
                    file_name = item['name']
                    break
        else:
            file_name = file
            file_id = None

            for item in self.files:
                if item['name'] == file_name:
                    file_id = item['id']
                    break

        if file_id is None or file_name is None:
            return

        request = self.service.files().get_media(fileId=file_id)
        fh = io.BytesIO()

        # Initialise a downloader object to download the file
        downloader = MediaIoBaseDownload(fh, request, chunksize=204800)
        done = False

        try:
            # Download the data in chunks
            while not done:
                status, done = downloader.next_chunk()

            fh.seek(0)

            # Write the received data to the file
            with open(file_name, 'wb') as f:
                shutil.copyfileobj(fh, f)

            print("File Downloaded")
            # Return True if file Downloaded successfully
            return True

        except Exception as e:
            print('An error occurred: %s' % e)
            return False

    def FileUpload(self, filepath, is_sheet=False):
        filename = re.findall("[\\\/]?[^\\\/]+", filepath)[-1].strip("\\\/")

        # Find the MimeType of the file
        mimetype = MimeTypes().guess_type(filename)[0]

        file_id = None

        if is_sheet:
            for item in self.files:
                if item['name'] == filename[:-4]:
                    file_id = item['id']
                    break
        else:
            for item in self.files:
                if item['name'] == filename:
                    file_id = item['id']
                    break

        try:
            media = MediaFileUpload(filepath, mimetype=mimetype)

            # Ficheiro não existe
            if file_id is None:
                if is_sheet:
                    # cretea file metadata
                    file_metadata = {'name': filename.replace('.csv', ''),
                                     'mimeType': "application/vnd.google-apps.spreadsheet",
                                     'parents': ["1b750jPt6xDnuDjTHcQR4TFgbnNSBJFPz"]}

                else:
                    file_metadata = {'name': filename,
                                     'mimeType': mimetype,
                                     'parents': ["1b750jPt6xDnuDjTHcQR4TFgbnNSBJFPz"]}

                # Create a new file in the Drive storage
                self.service.files().create(
                    body=file_metadata, media_body=media, fields='id').execute()

                print("File Uploaded.")

                if is_sheet is False and filename.endswith(".csv"):
                    self.FileUpload(filepath, is_sheet=True)

            else:
                if is_sheet:
                    # cretea file metadata
                    file_metadata = {'name': filename.replace('.csv', '')}
                else:
                    file_metadata = {'name': filename}

                # Create a new file in the Drive storage
                self.service.files().update(fileId=file_id,
                                            body=file_metadata, media_body=media, fields='id').execute()

                print("File Updated.")

                if is_sheet is False and filename.endswith(".csv"):
                    self.FileUpload(filepath, is_sheet=True)

        except Exception as e:
            print(e)
            # Raise UploadError if file is not uploaded.
            raise Exception("Can't Upload File.")

    def DeleteFile(self, file, by_id):
        """Permanently delete a file, skipping the trash.

        Args:
        service: Drive API service instance.
        file_id: ID of the file to delete.
        """
        if by_id:
            file_id = file
            file_name = None
            for item in self.files:
                if item['id'] == file_id:
                    file_name = item['name']
                    break
        else:
            file_name = file
            file_id = None

            for item in self.files:
                if item['name'] == file_name:
                    file_id = item['id']
                    break

        if file_id is None or file_name is None:
            return

        try:
            self.service.files().delete(fileId=file_id).execute()
        except Exception as e:
            print('An error occurred: %s' % e)

    def GetFiles(self):
        # request a list of first N files or
        # folders with name and id from the API.
        results = self.service.files().list(pageSize=100, fields="files(id, name, parents)").execute()
        items = results.get('files', [])

        return items

    def ListFiles(self, refresh=False):
        if refresh:
            self.files = self.GetFiles()

        # print a list of files
        print("\nHere's a list of files: \n")
        print(*self.files, sep="\n", end="\n\n")


if __name__ == "__main__":
    obj = DriveAPI()
    obj.ListFiles()

    argv = sys.argv

    if len(argv) == 2:
        i = 3
        argv2 = argv[1]
    elif len(argv) == 3:
        i = argv[1]
        argv2 = argv[2]
    else:
        i = int(input(
            "Enter your choice: 1 - Download file (by id), 2 - Download file (by name),\n 3 - Upload File, 4 - Delete File (by id), 5 - Delete File (by name).\n$ "))
        if i not in [1, 2, 3, 4, 5]:
            exit(0)

        argv2 = input('Insert identifier:\n$ ')

    if i == 1:
        f_id = argv2
        obj.FileDownload(f_id, by_id=True)
    elif i == 2:
        f_name = argv2
        obj.FileDownload(f_name, by_id=False)

    elif i == 3:
        f_path = argv2
        obj.FileUpload(f_path)

    elif i == 4:
        f_id = argv2
        obj.DeleteFile(f_id, by_id=True)
    elif i == 5:
        f_name = argv2
        obj.DeleteFile(f_name, by_id=False)

    obj.ListFiles(refresh=True)
