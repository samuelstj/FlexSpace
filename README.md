FlexSpace
=========
Armazenamento Eficiente de Dados em Memória Compartilhada Distribuída Tolerante a Faltas

Resumo
  Uma técnica muito empregada para tolerar falhas é a replicação. Nessa técnica, os processos (ou servidores) são replicados e protocolos são empregados para garantir a consistência dos dados manipulados nesses processos.
  
  As soluções existentes para Memória Compartilhada Distribuída Tolerante a Faltas, armazenam os dados em todos os servidores, subutilizando o espaço de armazenamento. Este trabalho propõe e implementa uma solução de armazenamento eficiente de dados em Memória Compartilhada Distribuída Tolerante a Faltas. A solução consiste em dividir o espaço de memória compartilhada em subespaços de forma que os dados sejam distribuídos estre esses subespaços, evitando a replicação desnecessária dos dados. Para análise da eficiência e desempenho da implementação, foram feitas simulações em um ambiente constituído por até 10 máquinas conectadas via rede em uma estrutura de aplicação distribuída.
