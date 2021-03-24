lexical_error = readmatrix("./results/sentiment/squared_error_lexical.txt");
lexical_error = lexical_error(:, 2);

ml_error = readmatrix("./results/sentiment/squared_error_ml.txt");
ml_error = ml_error(:, 2);

averaged_error = readmatrix("./results/sentiment/squared_error_averaged.txt");
averaged_error = averaged_error(:, 2);

x = 1:length(ml_error);

plot(x, lexical_error, x, ml_error, x, averaged_error);
ylim([0, 0.25]);
title("Sentiment Analysis Error");
xlabel("Sentence Index");
ylabel("Squared Error");
legend(["Lexical Analysis", "ML Analysis", "Combined Analysis"]);
