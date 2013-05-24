#!bin/python


from __future__ import print_function
import sys

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
        0 

        >>> search_ba("abaaabaabaa", "ab")
        0 4 7 

        >>> search_ba("mississippi", "ss")
        2 5 
    """
    border = calc_border_array(pattern + '$' + string)
    m = len(pattern)
    for i, b in enumerate(border):
        if b == m:
            print(str(i - 2*m) + ' ', end='')


def search_kmp(string, pattern):
    """
        >>> search_kmp("aba", "ab")
        0 

        >>> search_kmp("abaaabaabaa", "ab")
        0 4 7 

        >>> search_kmp("mississippi", "ss")
        2 5 
    """
    border = calc_border_array(pattern)
    shift = [0] + [x+1 for x in border] 
    i = 0
    j = 0
    m = len(pattern)
    while i <= len(string) - m + j:
        while j < m and (string[i] == pattern[j]) :
            i = i + 1
            j = j + 1
        if j == m: 
            print(str(i-m) + ' ', end='') 
        if j==0: 
            i = i + 1
        else:
            j = shift[j - 1]

def open_file_and_search(search_function, text_file, pattern):
    try:
        with file(text_file) as f:
            text = f.read()
            search_function(text, pattern)
    except IOError:
        print ("file not found:" + text_file)
