#!bin/python


from __future__ import print_function
import sys
import random
import time

def calc_border_array(string):
    """
        >>> calc_border_array("aaba")
        [0, 1, 0, 1]

        >>> calc_border_array("bbba")
        [0, 1, 2, 0]
        
        >>> calc_border_array("abaabaab")
        [0, 0, 1, 1, 2, 3, 4, 5]
    """
    border = [0] * len(string)
    border[0] = 0
    for i in xrange(1, len(string)):
        b = border[i-1]
        while b > 0 and not (string[i] == string[b]):
            b = border[b-1]
        if string[i] == string[b]:
            border[i] = b+1
        else:
            border[i] = 0       
            
    return border
    

def search_ba(string, pattern):
    """
        >>> search_ba("aba", "ab")
        [0]

        >>> search_ba("abaaabaabaa", "ab")
        [0, 4, 7]

        >>> search_ba("mississippi", "ss")
        [2, 5]
    """
    border = calc_border_array(pattern + '$' + string)
    m = len(pattern)
    results = []
    for i, b in enumerate(border):
        if b == m:
            results += [i - 2*m]
    return results


def search_kmp(string, pattern):
    """
        >>> search_kmp("aba", "ab")
        [0]

        >>> search_kmp("abaaabaabaa", "ab")
        [0, 4, 7]

        >>> search_kmp("mississippi", "ss")
        [2, 5]

        >>> search_kmp("acc", "ac")
        [0]
    """
    border = calc_border_array(pattern)
    shift = [0] + [x+1 for x in border] 
    i = 0
    j = 0
    m = len(pattern)
    results = []
    while i <= len(string) - m + j:
        while j < m and (string[i] == pattern[j]) :
            i = i + 1
            j = j + 1
        if j == m: 
            results += [i-m]
        if j==0: 
            i = i + 1
        else:
            j = shift[j] - 1
    return results

def open_file_and_search(search_function, text_file, pattern):
    try:
        with file(text_file) as f:
            text = f.read()
            results = search_function(text, pattern)
            print (results)
    except IOError:
        print ("file not found:" + text_file)

def benchmark_searches(search_function, text_file, pattern_length):
    try:
        with file(text_file) as f:
            text = f.read()
            
            #pattern_starts = [random.randint(0, len(text) - pattern_length) for x in xrange(0, 10000)]
            #patterns = [text[pattern_start : pattern_start + pattern_length] for pattern_start in pattern_starts] 
            pattern = "AAAAAAAAAB"   
            text_minimum = 100
            text_step = 10
            for i in xrange(0,10):

                current_text = text[:text_minimum + i * text_step]

                t_start = time.time()
                t = 0
                for i in xrange(1000):
                    search_function(current_text, pattern)
                    t += time.time() - t_start
                print(str(len(current_text)) + " " + str(t))
    except IOError:
        print ("file not found:" + text_file)    

