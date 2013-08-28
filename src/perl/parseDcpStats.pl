#!/usr/bin/env perl -w
# -------------------------------------------------------------------
# Copyright (c) 2007 University of Southern California.
# All rights reserved.                                            
#                                                                
# Redistribution and use in source and binary forms are permitted
# provided that the above copyright notice and this paragraph are
# duplicated in all such forms and that any documentation, 
# advertising materials, and other materials related to such 
# distribution and use acknowledge that the software was 
# developed by the Software Architecture Research Group at the 
# University of Southern Calfornia.  The name of the University may 
# not be used to endorse or promote products derived from this software 
# without specific prior written permission.
# 
# Any questions, comments, or or corrections should be mailed to the author 
# of this code, Chris Mattmann, at: mattmann@usc.edu
#
#
# THIS SOFTWARE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR  
# IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED 
# WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR  
# PURPOSE.
# 
# Author: Chris A. Mattmann
# Revision: 1.0
# -------------------------------------------------------------------

use strict;
use Getopt::Long;

MAIN:{

    my $statsFile;
    GetOptions('statsfile=s'=>\$statsFile);

    defined $statsFile || die "Usage: $0 -statsfile <file>";

    open(STATS, "<$statsFile");
    
    my %attr_hash;

    while(<STATS>){
	my $line = $_;
	
	if($line =~ /\ \*\*\*/){
	   $line =~ s/\*//g;
	   chomp $line;
	   print "line is $line\n";
	}

    }
    
    close(STATS);
}
