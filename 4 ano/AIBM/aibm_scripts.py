import pandas as pd

pd.set_option('max_columns', None)

default_date = 'NULL'
letters = ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'E', 'V', '-', '.']


def is_code(s):
    s = str(s)
    size = len(s)

    for i in range(size):
        if not s[i] in letters:
            return False

    return True


def search_for_codes_row(row):
    size = len(row)
    index = None

    for i in range(0, size, 2):
        if is_code(row[i]):
            index = i

    if index is None:
        print(row)

    return index


def create_struct(row, size):
    if size == 0:
        struct = [str(row[0]), row[1], "NULL"]
    else:
        struct = [str(row[2]), row[3], str(row[0])]

    return struct


def create_all_structs(tabela):
    size = len(tabela)
    list_structs = []

    for i in range(size):
        index = search_for_codes_row(tabela.iloc[i])

        for j in range(index, -1, -2):
            value = j - 2
            if value < 0:
                value = 0

            struct = create_struct(tabela.iloc[i][value: j + 3], j)
            index -= 2
            list_structs.append(struct)

    return list_structs


def script_row(file, list_row):
    file.write("(\"")
    file.write(list_row[0])
    file.write("\",\"")
    try:
        string = list_row[1].replace("\"", "\"\"")
        file.write(string)
    except AttributeError:
        file.write("----")

    file.write("\",")

    if list_row[2] == "NULL":
        file.write("NULL")
    else:
        file.write("\"")
        file.write(list_row[2])
        file.write("\"")

    file.write(")")


def script(cab, list_struct):
    file = open("MySQLWorkbench/script.sql", "w")

    file.write(cab)

    size = len(list_struct)
    for i in range(size):
        script_row(file, list_struct.iloc[i])
        if i == size - 1:
            file.write(";\n")
        else:
            file.write(",\n")

    file.close()


def isnum(s):
    try:
        float(s)
    except ValueError:
        return False
    else:
        return True


def format_date(date, b):
    # 01/01/18 00:14 ->
    # 2018-03-31 14:34:12 ->
    if date == "nan":
        return None

    ind = date.find('/')
    if ind > 0:
        ano = date[6:8]

        max_year = 19
        if b:
            max_year = 18

        if int(ano) >= max_year:
            s = "19"
        else:
            s = "20"

        s += ano
        s += "-"
        s += date[3:5]
        s += "-"
        s += date[:2]
        s += " "
        s += date[9:11]
        s += ":"
        s += date[12:14]
        s += ":00"

    else:
        s = date

    return s


def parse_date(tabela, b):
    size: int = len(tabela)

    datas = []
    for i in range(size):
        d = format_date(str(tabela.iloc[i][0]), b)

        if d is not None:
            datas.append(d)

    res = pd.DataFrame(datas, columns={'DTA'})
    return res


def script_date(cab, tabela):
    file = open("MySQLWorkbench/script_data.sql", "w")
    file.write(cab)

    size: int = len(tabela)

    file.write("(0,NULL")

    for i in range(size):
        file.write("),\n(")
        file.write(str(i + 1))
        file.write(",")

        row = str(tabela.iloc[i][0])
        file.write("\"")
        file.write(row)
        file.write("\"")

    file.write(");\n")


def change_to_str(cc):
    size = len(cc)

    for i in range(size):
        cc.iloc[i][2] = "NULL"

    return cc


def create_nulls(cc):
    size = len(cc)
    list_nulls = ["NULL"] * size
    nulls = pd.DataFrame(list_nulls, columns=['id_level_below'])
    return nulls


def main():
    c = pd.read_csv("icd9_hierarchy.csv", sep=',', low_memory=False)
    c1 = pd.read_csv("Python CSV/urgency_episodes_new_py.csv", sep=';', lineterminator='#',  low_memory=False)

    cc = c1[['COD_DIAGNOSIS', 'DIAGNOSIS']].rename(columns={'COD_DIAGNOSIS': 'id_level', 'DIAGNOSIS': 'desc_level'})
    aux = create_nulls(cc)
    cc['id_level_below'] = aux

    list_codes = create_all_structs(c)

    icd9 = pd.DataFrame(list_codes, columns=['id_level', 'desc_level', 'id_level_below']).append(cc)
    icd9 = icd9.drop_duplicates(subset=['id_level'])
    icd9.reset_index()

    script("use urgency;\n\nSET SQL_SAFE_UPDATES = 0;\nDELETE FROM Dim_Hierarchy_Diagnosis;\n\nINSERT INTO Dim_Hierarchy_Diagnosis (id_level, desc_level, id_prev_level) VALUES\n", icd9)


def main1():
    c1 = pd.read_csv("Python CSV/urgency_prescriptions_py.csv", sep=';', lineterminator='#', low_memory=False)
    c2 = pd.read_csv("Python CSV/urgency_episodes_new_.csv", sep=';', low_memory=False)
    c3 = pd.read_csv("Python CSV/urgency_procedures_py.csv", sep=';', lineterminator='#', low_memory=False)

    data1 = c1[['DT_PRESCRIPTION']].rename(columns={'DT_PRESCRIPTION': 'DTA'})
    data2 = c2[['DATE_OF_BIRTH']].rename(columns={'DATE_OF_BIRTH': 'DTA'})
    data3 = c2[['DT_ADMITION_URG']].rename(columns={'DT_ADMITION_URG': 'DTA'})
    data4 = c2[['DT_ADMITION_TRAIGE']].rename(columns={'DT_ADMITION_TRAIGE': 'DTA'})
    data5 = c2[['DT_DIAGNOSIS']].rename(columns={'DT_DIAGNOSIS': 'DTA'})
    data6 = c2[['DT_DISCHARGE']].rename(columns={'DT_DISCHARGE': 'DTA'})
    data7 = c3[['DT_PRESCRIPTION']].rename(columns={'DT_PRESCRIPTION': 'DTA'})
    data8 = c3[['DT_BEGIN']].rename(columns={'DT_BEGIN': 'DTA'})
    data9 = c3[['DT_CANCEL']].rename(columns={'DT_CANCEL': 'DTA'})

    data_2 = parse_date(data2, True)

    data = data_2.append(data1).append(data3).append(data4).append(data5).append(data6).append(data7).append(data8).append(data9)
    data = data.drop_duplicates(subset=['DTA'])
    data.reset_index()

    data = parse_date(data, False)
    data = data.drop_duplicates(subset=['DTA'])

    script_date("use urgency;\n\nSET SQL_SAFE_UPDATES = 0;\nDELETE FROM Dim_Date;\n\nINSERT INTO Dim_Date (id_date, date) VALUES\n", data)


if __name__ == '__main__':
    main()
    main1()
