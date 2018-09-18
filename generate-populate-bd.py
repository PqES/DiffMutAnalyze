from random import randint
import datetime
import hashlib

START_MUTANT_CODE_ID = 183
MUTANT_CODE_QTY = 10
DEAD_MUTANTS = []
START_USER_ID = 3
USER_QTY = 10
NAMES = ["Andre Williamson", "Rodolfo Schneider", "Erik Roy", "Sonia Henry", "Alton Day", "Christian Dixon", "Don Tate", "Darlene Harvey", "Alma Johnston", "Brandon Mills"]
START_MUTANT_REPORT_ID = 13
START_MUTANT_REPORT_ID_RANDOM_DIFFICULTY = START_MUTANT_REPORT_ID + 10
# 1.5 minutos
MIN_ANALYSIS_TIME = 1000 * 60 * 1.5
# 30 minutos
MAX_ANALYSIS_TIME = 1000 * 60 * 30 + 1
MIN_DIFFICULTY = 0
MAX_DIFFICULTY = 4
MIN_EQUIVALENCE = 0
MAX_EQUIVALENCE = 1
DATE_FORMAT = "%Y-%m-%d %H:%M:%S"



INSERT_USER_SQL = "INSERT INTO `equivalency_analyse`.`user` (`id`, `email`, `name`, `password`, `registration_date`) VALUES ('{0}', '{1}', '{2}', '{3}', '{4}');"

def writeUsers():
  userId = START_USER_ID
  for name in NAMES:
    username = name.replace(' ', '.').lower();
    email = username + "@email.com"
    passwordEncode = hashlib.sha256(username.encode('utf-8')).hexdigest()
    registrationDate = datetime.datetime.now().strftime(DATE_FORMAT)
    print(INSERT_USER_SQL.format(userId, email, name, passwordEncode, registrationDate))
    userId += 1


INSERT_REPORT_SQL = "INSERT INTO `equivalency_analyse`.`mutant_report` (`id`, `analysis_time`, `difficulty`, `equivalence`, `registration_date`, `mutant_code_id`, `user_id`) VALUES ('{0}', '{1}', '{2}', '{3}', '{4}', '{5}', '{6}');"

def writeNewMutantReport(mutantReportId, mutantCodeId, userId):
  analysisTime = randint(MIN_ANALYSIS_TIME, MAX_ANALYSIS_TIME)
  if mutantReportId > START_MUTANT_REPORT_ID_RANDOM_DIFFICULTY:
    difficulty = randint(MIN_DIFFICULTY, MAX_DIFFICULTY)
  else:
    difficulty = mutantReportId % 5
  equivalence = randint(MIN_EQUIVALENCE, MAX_EQUIVALENCE)
  registrationDate = datetime.datetime.now().strftime(DATE_FORMAT)
  print(INSERT_REPORT_SQL.format(mutantReportId, analysisTime, difficulty, equivalence, registrationDate, mutantCodeId, userId))


def writeAllMutantReports():
  mutantReportId = START_MUTANT_REPORT_ID
  for userId in range(START_USER_ID, START_USER_ID + USER_QTY):
    for mutantCodeId in range(START_MUTANT_CODE_ID, START_MUTANT_CODE_ID + MUTANT_CODE_QTY):
      if mutantCodeId not in DEAD_MUTANTS:
          writeNewMutantReport(mutantReportId, mutantCodeId, userId)
          mutantReportId += 1
    print()


# writeUsers()

writeAllMutantReports()
