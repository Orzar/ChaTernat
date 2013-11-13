from pprint import pprint

print ('<questionnaires>')

for iFile in range(16,54):
    questionFile = open("../xml/test_extract"+str(iFile)+".txt")
    i = 1
    for line in questionFile:
        if i % 7 == 0:
            solutions = line.split(':')[1]
            print ('<solutions ref="'+questionNumber+'">')
            solutions = solutions.lstrip()
            solutions = solutions.rstrip()
            solutions = solutions.split(' ')
            for solution in solutions:
                print ('<solution>' + solution.lstrip().rstrip() + '</solution>')
            print ('</solutions>')
            print ('</question>')
        elif i % 7 == 1:
            questionNumber = line.split('.')[0]
            ennonce = line.split('.')[1]
            print ('<question id="' + questionNumber.lstrip().rstrip() + '">')
            print ('<ennonce>' + ennonce.lstrip().rstrip() + '</ennonce>')
        elif i % 7 == 2:
            answerText = line.split(')')[1]
            print ('<reponses ref="'+ questionNumber +'">')
            print ('<reponse id="a">'+answerText.lstrip().rstrip()+'</reponse>')
        elif i % 7 == 3:
            answerText = line.split(')')[1]
            print ('<reponse id="b">'+answerText.lstrip().rstrip()+'</reponse>')
        elif i % 7 == 4:
            answerText = line.split(')')[1]
            print ('<reponse id="c">'+answerText.lstrip().rstrip()+'</reponse>')
        elif i % 7 == 5:
            answerText = line.split(')')[1]
            print ('<reponse id="d">'+answerText.lstrip().rstrip()+'</reponse>')
        elif i % 7 == 6:
            answerText = line.split(')')[1]
            print ('<reponse id="e">'+answerText.lstrip().rstrip()+'</reponse>')
            print ('</reponses>')
        i = i + 1

print ('</questionnaires>')
