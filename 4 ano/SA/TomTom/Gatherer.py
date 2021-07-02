import json
import os
import sys

from Incident import Incident

possible_files = ['braga_3.json', 'braganca_3.json', 'porto_3.json', 'coimbra_3.json', 'lisboa_3.json',
                  'setubal_3.json', 'evora_3.json', 'faro_3.json']


class Gatherer:
    def __init__(self, path, target, extension=".csv"):
        self.path = path
        self.target = target
        self.extension = extension
        self.data = []
        self.incidents = []
        self.header = "ID,Estradas,Numero_Estradas,Descricoes,Comprimento,Data_Inicio,Hora_Inicio,Data_Fim,Hora_Fim,Categoria,Magnitude,Coordenadas,Cidade,Versao,Diferenca\n"

    def gather_information(self):
        for filename in possible_files:
            if not os.path.exists(self.path + filename):
                continue
            else:
                old_data = json.load(open(self.path + filename))['incidents']

                for incident in old_data:
                    incident['city'] = filename[: -7]

                self.data.extend(old_data)

    def handle_information(self):
        for incident in self.data:
            incident_instance = Incident(incident, incident['entry_version'], incident['version_date'], incident['city'])
            self.incidents.append(incident_instance)

    def filter_data(self):
        dic = {}

        for incident in self.incidents:
            id_ = incident.id
            if id_ not in dic:
                dic[id_] = []

            dic[id_].append(incident)

        self.incidents = []

        for id_ in dic:
            max_version = -1
            max_incident = None

            for incident in dic[id_]:
                if incident.entry_version > max_version:
                    max_version = incident.entry_version
                    max_incident = incident

            self.incidents.append(max_incident)

    def process_data(self, print_all=False):

        i = 0
        j = 0
        while True:
            file = self.target+str(i)+self.extension

            with open(file, "w") as outfile:
                # outfile.write("\"sep=,\"\n")
                outfile.write(self.header)

                lines = 0

                for j in range(j, len(self.incidents)):
                    incident = self.incidents[j]
                    string, line = incident.to_csv(print_all)

                    if line + lines < 200000:
                        lines += line
                        outfile.write(string)
                    else:
                        break
            i += 1
            if j == len(self.incidents) - 1:
                break


if __name__ == '__main__':
    argv = sys.argv

    if len(argv) < 2:
        _path = os.getcwd() + "/JSONS/"
    else:
        _path = argv[1]

    if len(argv) < 3:
        _target = os.getcwd() + "/JSONS/Portugal"
    else:
        _target = argv[2]

    g = Gatherer(_path, _target)
    g.gather_information()
    g.handle_information()
    g.filter_data()
    g.process_data()
