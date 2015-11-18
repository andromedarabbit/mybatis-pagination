#!/bin/bash
readonly MYBATIS_PATH="lib"
echo "java -cp $MYBATIS_PATH/mybatis-generator-core-1.3.2.jar org.mybatis.generator.api.ShellRunner -configfile generatorConfig.xml -overwrite"
java -cp $MYBATIS_PATH/mybatis-generator-core-1.3.2.jar org.mybatis.generator.api.ShellRunner -configfile generatorConfig.xml -overwrite