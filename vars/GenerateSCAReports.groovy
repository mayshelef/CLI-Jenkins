def call() { 
    catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
        sh '''
            set -x
            export MEND_URL=${MEND_URL}
            export MEND_EMAIL=${MEND_EMAIL}
            export MEND_USER_KEY=${MEND_USER_KEY}
            WS_PROJECTTOKEN=$(grep -oP "(?<=token=)[^&]+" "${PWD}"/dep-results.txt)
            echo "Project Token: $WS_PROJECTTOKEN"
            
            if [ -z "$WS_PROJECTTOKEN" ]; then
                echo "No project token found, reports will not be generated" >&2
            else
                echo "Creating Project Risk Report"
                curl -o "${PWD}/riskreport.pdf" -X POST "${MEND_URL}/api/v1.4" -H "Content-Type: application/json" -d '{"requestType":"getProjectRiskReport","userKey":"'"${MEND_USER_KEY}"'","projectToken":"'"${WS_PROJECTTOKEN}"'"}'
                echo "Creating Project Inventory Report"
                curl -o "${PWD}/inventoryreport.xlsx" -X POST "${MEND_URL}/api/v1.4" -H "Content-Type: application/json" -d '{"requestType":"getProjectInventoryReport","userKey":"'"${MEND_USER_KEY}"'","projectToken":"'"${WS_PROJECTTOKEN}"'"}'
                echo "Creating Project Due Diligence Report"
                curl -o "${PWD}/duediligencereport.xlsx" -X POST "${MEND_URL}/api/v1.4" -H "Content-Type: application/json" -d '{"requestType":"getProjectDueDiligenceReport","userKey":"'"${MEND_USER_KEY}"'","projectToken":"'"${WS_PROJECTTOKEN}"'"}'
            fi
        '''
        archiveArtifacts artifacts: "riskreport.pdf, inventoryreport.xlsx, duediligencereport.xlsx, spdxreport.json", fingerprint: true
    }
}
