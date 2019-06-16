#Copyright (c) 2019 BusyMachines
#
#Licensed under the Apache License, Version 2.0 (the "License");
#you may not use this file except in compliance with the License.
#You may obtain a copy of the License at
#
#  http://www.apache.org/licenses/LICENSE-2.0
#
#Unless required by applicable law or agreed to in writing, software
#distributed under the License is distributed on an "AS IS" BASIS,
#WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#See the License for the specific language governing permissions and
#limitations under the License.

#!/usr/bin/env bash

CACHE=~/.ivy2/local/com.busymachines/pureharm-*
LOCAL=~/.ivy2/cache/com.busymachines/pureharm-*

echo "purging local ivy cache of com.busymachines artifacts"
echo "@ $LOCAL"
ls -l $LOCAL
echo "----------------"
echo "@ $CACHE"
ls -l $CACHE
echo "----------------"
rm -v -rf $LOCAL
rm -v -rf $CACHE
