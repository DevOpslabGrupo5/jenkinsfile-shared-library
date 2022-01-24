def cd(Map pipelineParameters){
      
    pipeline {
        agent any
        environment{
            NEXUS_USER = credentials('usernexusadmin')
            NEXUS_PASSWORD = credentials('passnexusadmin')
            VERSION = '0.0.17'
            FINAL_VERSION = '1.0.0'
        }
        stages{
            stage("7: gitDiff"){
                //- Mostrar por pantalla las diferencias entre la rama release en curso y la rama
                //master.(Opcional)
                steps {
                    sh "echo 'gitDiff'"
                }
            }
            stage("8: nexusDownload"){
                //- Descargar el artefacto creado al workspace de la ejecución del pipeline.
                steps {                   
                    sh 'sleep 5 '
                    sh 'curl -X GET -u $NEXUS_USER:$NEXUS_PASSWORD http://nexus:8081/repository/devops-usach-nexus/com/devopsusach2020/DevOpsUsach2020/$VERSION/DevOpsUsach2020-$VERSION.jar -O'
                }
            }
            stage("9: run"){
                //- Ejecutar artefacto descargado.
                steps {
                    sh 'nohup java -jar DevOpsUsach2020-$VERSION.jar & >/dev/null'
                }
            }
            stage("9: test"){
                //- Realizar llamado a microservicio expuesto en local para cada uno de sus
                //métodos y mostrar los resultados.
                steps {
                    sh "sleep 30 && curl -X GET 'http://localhost:8081/rest/mscovid/test?msg=testing'"
                }
            }
            stage("9: gitMergeMaster"){
                //- Realizar merge directo hacia la rama master.
                //- Ejecutar sólo si todo lo demás resulta de forma exitosa.
                steps {
                    sh "echo 'gitMergeMaster'"
                }
            }
            stage("10: gitMergeDevelop"){
                //- Realizar merge directo hacia rama develop.
                //- Ejecutar sólo si todo lo demás resulta de forma exitosa
                steps {
                    sh "echo 'gitMergeDevelop'"
                }
            }
            stage("11: gitTagMaster"){
                //- Crear tag de rama release en rama master.
                //- Ejecutar sólo si todo lo demás resulta de forma exitosa.
                steps {
                    sh "echo 'gitTagMaster'"
                }
            }
        }
    }
}