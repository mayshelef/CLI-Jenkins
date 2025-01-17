def call() { 
    echo 'Run Mend dependencies scan'
    catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
        sh '''
            set -x
            export MEND_URL=${MEND_URL}
            export MEND_EMAIL=${MEND_EMAIL}
            export MEND_USER_KEY=${MEND_USER_KEY}
            export repo=$(basename -s .git $(git config --get remote.origin.url))
            export branch=$(git rev-parse --abbrev-ref HEAD)
            /usr/local/bin/mend dep -u -s "*//${JOB_NAME}//${repo}_${branch}" --fail-policy --non-interactive --export-results dep-results.txt
            dep_exit=$?
            if [[ "$dep_exit" == "9" ]]; then
                echo "[warning]  Dependency scan policy violation"
            else
                echo "No policy violations found in dependencies scan"
            fi          
        '''
    }
    archiveArtifacts artifacts: "dep-results.txt", fingerprint: true
}
