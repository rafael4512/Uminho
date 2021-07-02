import os
import sys
from Gatherer import Gatherer
from Store_Data import DriveAPI

if __name__ == '__main__':
    argv = sys.argv

    if len(argv) < 2:
        _path = os.getcwd() + "/JSONS/"
    else:
        _path = argv[1]

    g = Gatherer(_path, _path + "Portugal")
    g.gather_information()
    g.handle_information()
    g.filter_data()
    g.process_data()

    g = Gatherer(_path + "realtime/", _path + "realtime/Portugal_rt")
    g.gather_information()
    g.handle_information()
    g.filter_data()
    g.process_data(print_all=True)

    obj = DriveAPI()
    obj.ListFiles()

    for file in os.listdir(_path):
        if file.startswith("Portugal"):
            print(file)

    for file in os.listdir(_path + "realtime"):
        if file.startswith("Portugal"):
            print(file)
    if len(argv) == 1:
        i = int(input("Upload? "))
    else:
        i = 1

    if i != 0:

        for file in os.listdir(_path):
            if file.startswith("Portugal"):
                obj.FileUpload(_path + file)

        for file in os.listdir(_path + "realtime"):
            if file.startswith("Portugal"):
                obj.FileUpload(_path + "realtime/" + file)

        obj.ListFiles()
