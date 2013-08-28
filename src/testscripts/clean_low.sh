#!/bin/tcsh
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

rm -rf num_users_*cons* num_users_*scal*
rm -rf geographic_distribution_dep* geographic_distribution_eff* geographic_distribution_scal*
rm -rf *intervals_eff* *intervals_sca* *intervals_dep*
rm -rf *user_types_dep* *user_types_eff* *user_types_cons*
rm -rf *total_vol*dep* *total_vol*cons* *total_vol*eff*
rm -rf *volume_per*dep* *volume_per*sca*
