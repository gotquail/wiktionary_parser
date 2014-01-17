import sys
import httplib
import re
import Queue
import threading

NUM_THREADS = 1

queue_in = Queue.Queue()
queue_out = Queue.Queue()

class ThreadWikiQuery(threading.Thread):
    def __init__(self, queue_in, queue_out):
        threading.Thread.__init__(self)
        self.queue_in = queue_in
        self.queue_out = queue_out

    def run(self):
        while True:
            print "123" #todo


def main():

    if (len(sys.argv) != 3):
        print "Usage: python simple_parser.py WIKTIONARY_FILE_IN FILE_OUT"
        return 1

    wiki_file_path = sys.argv[1]
    out_file_path = sys.argv[2]

    f_in = open(wiki_file_path, 'r');
    f_out = open(out_file_path, 'w');
    
    conn = httplib.HTTPConnection("en.wiktionary.org")

    count = 0
    for line in f_in:
        print "count: " + str(count)
        count += 1
        parts = line.split('#', 1)
        wiki_markup = parts[1].strip();
        wiki_markup = wiki_markup.replace(" ", "%20")
        query = "/w/api.php?format=json&action=parse&prop=text&text=" + wiki_markup
        # print query
        conn.request("GET", query)
        r1 = conn.getresponse()
        # print r1.status, r1.reason
        data1 = r1.read()
        # print data1

        
        m = re.search('"text":{"\*":"(.*?)\\\\n\\\\n\\\\n', data1);
        if m:
            # print m.group(1)
            # print ""
            f_out.write(parts[0] + "# " + m.group(1) + "\n")
        else:
            print "no match: " + line

        # # f_out.write(line)

    conn.close()
    f_in.close();
    f_out.close();
    return 0


if __name__ == "__main__":
    sys.exit(main())