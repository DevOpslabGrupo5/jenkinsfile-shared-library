def call(Map pipelineParameters){
        
    pipeline {
        agent any
        environment{
            NEXUS_USER = credentials('usernexusadmin')
            NEXUS_PASSWORD = credentials('passnexusadmin')
            VERSION = '0.0.15'
            FINAL_VERSION = '1.0.0'
        }
        stages{
            stage('Integracion'){
                steps{
                    sh "echo 'branchname: '" + BRANCH_NAME
                    sh 'printenv'
                }
            }
        }
    }
}