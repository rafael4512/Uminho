import sys
import os

from Incidents import Incidents

if __name__ == '__main__':
    argv = sys.argv

    if len(argv) < 2:
        _area = 'Braga'
    else:
        _area = argv[1]

    if len(argv) < 3:
        _path = os.getcwd() + "/JSONS/"
    else:
        _path = argv[2]

    incidents = Incidents(_area, _path, path_real_time=_path + "realtime/")
    incidents.collect_new_data()
    incidents.process_data()
    incidents.write_data_to_json()

    incidents.process_new_data()
    incidents.write_new_data_to_json()
