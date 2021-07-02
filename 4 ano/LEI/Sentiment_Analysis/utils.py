# Global variables regarding scales
OLD_MAX = 1
OLD_MIN = -1
NEW_MAX = 5
NEW_MIN = 1
OLD_RANGE = 2
NEW_RANGE = 4

# Convert scale [-1,1] to scale [1,5]
def convert_scale(compound):
    new_value = (((compound - OLD_MIN) * NEW_RANGE) / OLD_RANGE) + NEW_MIN
    return new_value
