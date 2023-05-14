
import sys


def func(a,b):
    print(a,b)


if __name__ == '__main__':
    a = []
    for i in range(1, len(sys.argv)):
        a.append((int(sys.argv[i])))
    func(a[0],a[1])