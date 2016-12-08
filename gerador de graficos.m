close all; clear; clc;

arquivoName = 'C:\Users\FranciscoCarlos\Desktop\moquette-master\database\2016-12-06.txt';

abrirArquivo = fopen(arquivoName,'r');

%# Get file size.
fseek(abrirArquivo, 0, 'eof');
fileSize = ftell(abrirArquivo);
frewind(abrirArquivo);
%# Read the whole file.
data = fread(abrirArquivo, fileSize, 'uint8');
%# Count number of line-feeds and increase by one.
numLines = sum(data == 10) + 1;
frewind(abrirArquivo);
numLines = numLines - 1;

n = 1;
values = cell(numLines,11);
while ~feof(abrirArquivo)
    tline = fgetl(abrirArquivo);
    values(n,:) = strsplit(tline,{' ','='});
    disp(values(n));
    n = n+1;
end;

fclose(abrirArquivo);

horario = values(:,1);
umidade = str2double(values(:,3));
chuva = str2double(values(:,5));
temperatura = str2double(values(:,7));
vazao = str2double(values(:,9));
comando = values(:,11);

estado = zeros(numLines,1);
for i=1:numLines
    if(strcmp(comando(i),'ligar'))
        estado(i) = 1;
    end;
    if(strcmp(comando(i),'desligar'))
        estado(i) = 0;
    end;
end;


%sinalSaida = [zeros(32,1) sinalSaida];

figure(1);
%subplot(3,1,1);
hold on;
subplot(2,2,1);
plot(umidade,'b');
axis([1 numLines 0 1200]);
legend('umidade');
title('Monitoramente de umidade');
subplot(2,2,2);
plot(chuva,'r');
axis([1 numLines 0 1200]);
legend('chuva');
title('Monitoramente de chuva');
%legend('Sina)','LMS');
%hold off;

subplot(2,2,4);
plot(estado,'k');
title('Estado da válvula');
legend('estado valvula');
axis([1 numLines 0 1.2]);
%figure(2);

subplot(2,2,3);
plot(temperatura,'m');
title('Monitoramento da temperatura');
legend('temperatura');
axis([1 numLines 0 50]);


%legend('1 para aberto - 0 para fechado');
%subplot(3,1,2);
%hold on;
%plot(sinal,'b');
%plot(sinalSaidaLMS,'r');
%title('LMS antigo');
%legend('Sinal','LMS');
%hold off;

%subplot(3,1,3);
%hold on;
%plot(sinalSaidaLMS,'b');
%plot(sinalSaida,'r');
%title('LMS antigo');
%legend('LMS Antigo','LMS novo');
%hold off;



