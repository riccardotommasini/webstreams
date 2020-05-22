#!/usr/bin/python3

from rdflib import Graph, plugin
from rdflib.serializer import Serializer
import os

res="./_resources/"
streams="./_streams/"


def cd(dir):
    print("cd")
    print(dir)
    for f in os.listdir(dir):
        if os.path.isdir(os.path.join(dir, f)):
            cd(os.path.join(dir, f))
        elif f.endswith(".ttl"):
            convert(dir,f)

def convert(dir,file):
    print("convert")
    print(dir)
    print(file)
    g = Graph()
    out=file.replace("ttl", "jsonld")
    print(os.path.join(dir, file))
    g.parse(os.path.join(dir, file), format="ttl")
    g.serialize(destination=os.path.join(dir, out), format='json-ld',indent=4)



if __name__ == "__main__":
    # execute only if run as a script
    cd(res)
