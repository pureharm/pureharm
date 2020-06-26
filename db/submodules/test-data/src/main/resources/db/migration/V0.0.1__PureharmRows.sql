--
-- Copyright (c) 2019 BusyMachines
--
-- See company homepage at: https://www.busymachines.com/
--
-- Licensed under the Apache License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
--
-- http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
--
create TABLE "pureharm_rows"(
    "id"   VARCHAR NOT NULL PRIMARY KEY,
    "byte" SMALLINT NOT NULL,
    "int" INT NOT NULL,
    "long" BIGINT NOT NULL,
    "big_decimal" DOUBLE PRECISION NOT NULL,
    "string" VARCHAR NOT NULL,
    "jsonb_col" JSONB NOT NULL,
    "opt_col" VARCHAR NULL
);
