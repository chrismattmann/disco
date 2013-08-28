#!/bin/sh
# Copyright (c) 2006 University of Southern California.
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
#

./disco analysis --dcpPath ../dcp \
--propFile ../etc/less.disco.properties \
--selector edu.usc.softarch.disco.selection.OptimizingSelector \
--analyzer edu.usc.softarch.disco.analysis.kmeans.ExhaustiveKMeans \
--answerKeyDirPath ../answers/ \
--scenarioDirPath ../scenarios/

./disco analysis --dcpPath ../dcp \
--propFile ../etc/medium.disco.properties \
--selector edu.usc.softarch.disco.selection.OptimizingSelector \
--analyzer edu.usc.softarch.disco.analysis.kmeans.ExhaustiveKMeans \
--answerKeyDirPath ../answers/ \
--scenarioDirPath ../scenarios/

./disco analysis --dcpPath ../dcp \
--propFile ../etc/more.disco.properties \
--selector edu.usc.softarch.disco.selection.OptimizingSelector \
--analyzer edu.usc.softarch.disco.analysis.kmeans.ExhaustiveKMeans \
--answerKeyDirPath ../answers/ \
--scenarioDirPath ../scenarios/