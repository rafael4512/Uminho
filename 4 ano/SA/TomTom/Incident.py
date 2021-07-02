from datetime import datetime
import re
import unidecode

icon_cat = {
    0: "Desconhecido",
    1: "Acidente",
    2: "Nevoeiro",
    3: "Condicoes perigosas",
    4: "Chuva",
    5: "Gelo",
    6: "Congestionamento",
    7: "Faixa fechada",
    8: "Estrada fechada",
    9: "Trabalhos na estrada",
    10: "Vento",
    11: "Inundacao",
    14: "Veiculo avariado"
}


mag = {
    0: "Desconhecido",
    1: "Baixo",
    2: "Moderado",
    3: "Elevado",
    4: "Indefinido"
}


class Incident:

    def __init__(self, incident, entry_version, timestamp, city=""):
        self.__type = incident['type']
        self.__properties = incident['properties']
        self.__geometry = incident['geometry']

        self.__id = self.__properties['id']
        self.__length = self.__properties['length']

        self.__entry_version = entry_version
        self.__version_date = timestamp
        self.__city = city

    @property
    def to_json(self):
        return {"type": self.__type,
                "entry_version": self.__entry_version,
                "version_date": self.__version_date,
                "properties": self.__properties,
                "geometry": self.__geometry
                }

    def to_csv(self, print_all=False):
        string = ""

        roads = self.__properties['roadNumbers']
        roads_from = re.findall('N\d+|A\d+|IC\d+', self.__properties['from'])
        roads_to = re.findall('N\d+|A\d+|IC\d+', self.__properties['from'])

        roads = list(set(roads + roads_from + roads_to))

        def date_diff(begin, end):
            if end is None:
                return ""

            begin = datetime.strptime(begin[:-1], "%Y-%m-%dT%H:%M:%S")
            end = datetime.strptime(end[:-1], "%Y-%m-%dT%H:%M:%S")

            return abs((end - begin).days) * 24 + abs((end - begin).seconds / 3600)

        def get_date_time(date_time):
            if date_time is not None:
                d = datetime.strptime(date_time[:-1], "%Y-%m-%dT%H:%M:%S")

                return datetime.strftime(d, "%Y-%m-%d"), datetime.strftime(d, "%H")
            else:
                return "", ""

        difference = date_diff(self.__properties['startTime'], self.__properties['endTime'])
        if difference != "":
            difference = round(difference * 2) / 2

        date_s, time_s = get_date_time(self.__properties['startTime'])
        date_e, time_e = get_date_time(self.__properties['endTime'])

        descriptions = []
        for event in self.__properties['events']:
            descriptions.append(event['description'])

        descriptions = str(descriptions).replace("[", "").replace("]", "").replace("'", "").replace(", ", ",")
        num_roads = len(roads)
        roads = str(roads).replace("[", "").replace("]", "").replace("'", "").replace(", ", ",")

        lines = 0
        step = 3
        if print_all:
            step = 1

        for coord in self.__geometry['coordinates'][::step]:
            lines += 1
            string += unidecode.unidecode(str(self.__id) + ",\"" + roads + "\"," + str(num_roads) + ",\"" + descriptions + "\"," + str(
                self.__properties['length']) + "," + date_s + "," + time_s + "," + date_e + "," + time_e + "," + icon_cat[self.__properties['iconCategory']] + "," + mag[
                          self.__properties['magnitudeOfDelay']] + ",\"" + str(
                coord[1]) + "," + str(coord[0]) + "\"," + self.__city.capitalize() + "," + str(self.__entry_version) + "," + str(difference) + "\n")

        return string, lines

    @property
    def id(self):
        return self.__id

    @property
    def length(self):
        return self.__length

    @property
    def entry_version(self):
        return self.__entry_version

    def __eq__(self, other):
        return self.id == other.id and self.length == other.length

    def __hash__(self):
        return hash((self.id, self.length))
