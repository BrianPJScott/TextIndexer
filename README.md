Indexer
A program to create and export an index for one or more text files

This is a menu driven application which parses through a text file and creates an index. Each index entry will contain:

1 word
1 definition for that word
1+ page numbers where that work occurs in text being indexed
To run from command line: java --enable-preview -cp ./indexer.jar ie.atu.sw.Runner (Note: as virtual threading (project loom) is a preview feature at the tie of compilation, the '--enable-preview' switch must be used).

Menu options:
(1) Configure Text File - Prompts the user for the path to the plain text file to be indexed.
(2) Configure Dictionary - Prompts the user for the path to the dictionary containing definitions for words to be parsed and indexed.
(3) Configure Common Words - Prompts the user for the path to the file containing a list of words to be excluded from the index.
(4) Specify Output File - Prompts the user for the path to the destination output file.
(5) Execute & Write to Output File - Runs the parser, writing results to the specified output file.
(6) Execute & Print to Screen - Runs the parser, echoing results to the screen (System.out)
(7) Quit - Exit application

Notes:
One word has one definition, duplicate words will not defined.
One word may appear on many pages, but will only be listed once for each page, regardless of number of occurrences on that page.
If the specified output file exists, it will be overwritten. If not, it will be created.

References: https://vlegalwaymayo.atu.ie/course/view.php?id=5927 https://docs.oracle.com/javase/7/docs/api/java/util/Collections.html https://www.baeldung.com/javadoc-version-since https://app.diagrams.net/
