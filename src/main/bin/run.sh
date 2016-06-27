#!/bin/sh
#!/bin/bash
# resolve links - $0 may be a softlink
THIS="$0"
# copy mr config file

# get arguments
COMMAND=$1
shift

# some directories
THIS_DIR=`dirname "$THIS"`
STATS_HOME=`cd "$THIS_DIR" ; pwd`

JAVA=$JAVA_HOME/bin/java
JAVA_HEAP_MAX=-Xmx8000m

CLASSPATH=".:"${STATS_CONF_DIR:=$STATS_HOME/conf}
CLASSPATH=${CLASSPATH}:$JAVA_HOME/lib/tools.jar

for f in $STATS_HOME/*.jar; do
  CLASSPATH=${CLASSPATH}:$f;
done

# add libs to CLASSPATH
for f in $STATS_HOME/lib/*.jar; do
  CLASSPATH=${CLASSPATH}:$f;
done

# figure out which class to run
if [ "$COMMAND" = "trainLM" ]; then
    CLASS=com.yeezhao.guizhou.train.lang.LangMdlLocalTrainCli
#elif [ "$COMMAND" = "refineLM" ]; then
#    CLASS=com.yeezhao.guizhou.train.lang.LangMdlRefineCli
elif [ "$COMMAND" = "trainEM" ]; then
    CLASS=com.yeezhao.guizhou.train.err.ErrMdlTrainCli
elif [ "$COMMAND" = "convertLM" ]; then
    CLASS=com.yeezhao.guizhou.train.lang.LangMdlLoadCli
elif [ "$COMMAND" = "convertPYIndex" ]; then
    CLASS=com.yeezhao.guizhou.train.PinyinIndexBuilder
elif [ "$COMMAND" = "convertEM" ]; then
    CLASS=com.yeezhao.guizhou.train.err.ErrMdlLoadCli
elif [ "$COMMAND" = "spellcheck" ]; then
    CLASS=com.yeezhao.guizhou.app.SpellCheckerCli
elif [ "$COMMAND" = "start" ]; then
    CLASS=com.yeezhao.guizhou.server.SpellCheckRpcServer
elif [ "$COMMAND" = "demo" ]; then
    CLASS=com.yeezhao.guizhou.demo.SpellCheckerClient
elif [ "$COMMAND" = "baseline" ]; then
    CLASS=com.yeezhao.guizhou.app.BaselineCli
elif [ "$COMMAND" = "l2rtrain" ]; then
    CLASS=com.yeezhao.guizhou.lr.LearnToRankTrainCli
elif [ "$COMMAND" = "l2rgen" ]; then
    CLASS=com.yeezhao.guizhou.lr.LearnToRankGenCli
elif [ "$COMMAND" = "gencorpus" ]; then
    CLASS=com.yeezhao.guizhou.corpus.CorpusCli
elif [ "$COMMAND" = "findtc" ]; then
    CLASS=com.yeezhao.guizhou.refinetools.EMRefinerCli
elif [ "$COMMAND" = "stop" ]; then
    stop_rpc
    exit 1
elif [ "$COMMAND" = "runClient" ]; then
    CLASS=com.yeezhao.guizhou.client.ClientRunner
elif [ "$COMMAND" = "EvalTest" ]; then
    CLASS=com.yeezhao.guizhou.client.EvalTest
elif [ "$COMMAND" = "HBaseTest" ]; then
    CLASS=com.gy.crawler.test.HBaseTest
fi

# run it
params=$@
"$JAVA" -Dfile.encoding=UTF-8 -Djava.awt.headless=true $JAVA_HEAP_MAX -classpath "$CLASSPATH" $CLASS $params
