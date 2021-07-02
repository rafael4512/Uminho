import json
import os
import requests
from datetime import datetime as dt


from Incident import Incident

url = 'https://api.tomtom.com/traffic/services/5/incidentDetails?bbox='
key = "YJWNf0XyRvoRxBDjMDXG3fAhRTLOf2MW"
key_for_url = '&key=' + key
url_fields_lang = '&fields=%7Bincidents%7Btype%2Cgeometry%7Btype%2Ccoordinates%7D%2Cproperties%7Bid%2CiconCategory' \
                  '%2CmagnitudeOfDelay%2Cevents%7Bdescription%2Ccode%7D%2CstartTime%2CendTime%2Cfrom%2Cto%2Clength' \
                  '%2Cdelay%2CroadNumbers%2Caci%7BprobabilityOfOccurrence%2CnumberOfReports%2ClastReportTime%7D%7D%7D' \
                  '%7D&language=pt-PT'

areas_data = {'Braga': ('braga_3.json', 41.247, -8.916, 42.100, -7.829),
              'Braganca': ('braganca_3.json', 41.272, -7.827, 42.060, -6.600),
              'Porto': ('porto_3.json', 40.424, -8.828, 41.222, -7.708),
              'Coimbra': ('coimbra_3.json', 39.583, -9.125, 40.433, -8.092),
              'Lisboa': ('lisboa_3.json', 38.667, -9.489, 39.595, -8.413),
              'Setubal': ('setubal_3.json', 37.914, -9.220, 38.685, -8.112),
              'Evora': ('evora_3.json', 37.885, -8.148, 38.760, -7.081),
              'Faro': ('faro_3.json', 36.996, -9.012, 37.853, -7.957),
              }


class Incidents:

    def __init__(self, area, path, path_real_time=None):
        self.__filename, self.__min_lat, self.__min_long, self.__max_lat, self.__max_long = areas_data[area]
        self.__path = path
        self.__filepath = path + self.__filename

        self.__old_data = self.__collect_old_data()

        if path_real_time:
            self.__path_rt = path_real_time
            self.__filepath_rt = path_real_time + self.__filename
        else:
            self.__path_rt = None
            self.__filepath_rt = None

        self.__new_data = None
        self.__processed_data = None
        self.__processed_new_data = None

    @property
    def filename(self):
        return self.__filename

    # ------------ Collect Data ------------

    def __collect_old_data(self):
        if not os.path.exists(self.__filepath):
            os.makedirs(self.__path, exist_ok=True)
            old_data = []
        else:
            old_data = json.load(open(self.__filepath))['incidents']

        return old_data

    def collect_new_data(self):
        data = self.__request_data()

        if 'detailedError' in data:
            raise Exception('Erro\n' + json.dumps(data['detailedError'], indent=2))

        self.__new_data = data['incidents']

    @property
    def new_data(self):
        return self.__new_data

    @new_data.setter
    def new_data(self, value):
        self.__new_data = value

    def __request_data(self):
        url_bbox = '%2C'.join([str(self.__min_long), str(self.__min_lat), str(self.__max_long), str(self.__max_lat)])
        res = requests.get(''.join([url, url_bbox, url_fields_lang, key_for_url]))
        content = res.content.decode('utf-8')
        return json.loads(content)

    # ------------ Process Data------------

    def process_data(self):
        old_incident_instances = self.__process_old_incidents()
        new_incident_instances = self.__process_new_incidents(old_incident_instances)

        all_incident_instances = old_incident_instances + new_incident_instances
        self.__processed_data = {'incidents': [incident.to_json for incident in all_incident_instances]}

    def process_new_data(self):
        new_incident_instances = self.__process_new_incidents([])

        self.__processed_new_data = {'incidents': [incident.to_json for incident in new_incident_instances]}

    def __process_new_incidents(self, old_incident_instances):
        incidents = []

        for incident in self.__new_data:
            entry_version = self.__calculate_version(incident['properties']['id'])

            incident_instance = Incident(incident, entry_version, dt.now().strftime("%d-%m-%Y %H:%M:%S"))

            if incident_instance not in old_incident_instances:
                incidents.append(incident_instance)

        return incidents

    def __process_old_incidents(self):
        incidents = []

        for incident in self.__old_data:
            incident_instance = Incident(incident, incident['entry_version'], incident['version_date'])
            incidents.append(incident_instance)

        return incidents

    def __calculate_version(self, incident_id):
        id_duplication_counter = 0

        for incident in self.__old_data:
            if incident['properties']['id'] == incident_id:
                id_duplication_counter += 1

        return id_duplication_counter

    # ------------ Write Data ------------

    def write_data_to_json(self):
        if not os.path.exists(self.__path):
            os.makedirs(self.__path)

        with open(self.__filepath, "w") as outfile:
            json.dump(self.__processed_data, outfile, indent=2)

    def write_new_data_to_json(self):
        if self.__filepath_rt:
            if not os.path.exists(self.__path_rt):
                os.makedirs(self.__path_rt)

            with open(self.__filepath_rt, "w") as outfile:
                json.dump(self.__processed_new_data, outfile, indent=2)
