#!/usr/bin/perl -w
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
require "newgetopt.pl";
use vars qw($opt_h $opt_s $opt_k $opt_r $opt_f);

sub init() {
	&NGetOpt( "h", "s:s", "k:s", "r:i", "f:s" );
	usage() if ( $opt_h || !$opt_s || !$opt_k || !$opt_r || !$opt_f );

}

sub usage() {
	print STDERR << "EOF";

    Runs a scalability benchmark by executing the SelectorTestRunner a
    number of different times.

    usage: $0 [-shkr] [-f file]

     -h        : this (help) message
     -s        : class name of connector selector to use
     -k        : size of the DCP knowledge base
     -r        : number of connector selections to execute
     -f file   : distribution scenario file

    example: $0 -s edu.usc.softarch.disco.selection.OptimizingSelector -k 100 -r 10000 -f file
    
EOF
	exit;
}

MAIN: {

	init();
	my $kb_size      = $opt_k;
	my $selector     = $opt_s;
	my $scenarioFile = $opt_f;
	my $cmd = "java -Djava.util.logging.config.file=../etc/logging.properties ";
	$cmd .=
	  "-Djava.ext.dirs=../lib edu.usc.softarch.disco.tools.SelectorTestRunner ";
	$cmd .= "--selector " . $selector . " --scenarioPath " . $scenarioFile;
	$cmd.=" --dcpDir ../dcp_test_data/" . $kb_size
	  . " --props ../etc/disco.properties --numRuns ";

	for ( my $i = 1 ; $i <= $opt_r ; $i += 1000 ) {
		my $localCmd =
		  $cmd . $i . " > sdata/" . $kb_size . "/run" . $i . ".txt";

		#print "Command is: ".$localCmd."\n";
		system($localCmd);
	}

}

