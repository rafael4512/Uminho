from read_inquiry_from_mongodb import get_inquiry
def get_properties(inquiry_name):
    inq = get_inquiry(inquiry_name)
    md = inq.get('module')
    p = inq.get('questions')
    properties= []
    for x, y in p.items():
        properties.append(x)
    return md, properties

