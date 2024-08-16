The algorithm uses java.util.BitSet to memorize counted addresses. We need to use two BitSet instances as one allows us to store
info about half of all possible IP addresses (Integer.MAX_VALUE = 0x7fffffff).\
\
It uses the same amount of memory in both worst (all possible IPs listed in a file) and best cases (no IPs in a file) ~550Mb.
It could be optimized by using another data structure for scenarios when a file contains a small amount of IPs, but it's
the most optimal way for the worst case.\
\
Regarding speed, it's an optimal way as the access complexity (get and set) of BitSet is O(1).
