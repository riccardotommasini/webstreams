package it.polimi.deib.rsp.webstreams.geldt;

import com.taxonic.carml.logical_source_resolver.CsvResolver;
import com.taxonic.carml.model.LogicalSource;
import com.univocity.parsers.common.record.Record;
import com.univocity.parsers.csv.CsvFormat;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import java.io.StringReader;

public class MyCsvResolver extends CsvResolver {

    private final String[] headers;

    CsvParserSettings settings = new CsvParserSettings();


    public MyCsvResolver(String[] headers, char delimiter) {
        this.headers = headers;
        settings.setHeaderExtractionEnabled(false);
        settings.setHeaders(headers);
        settings.setLineSeparatorDetectionEnabled(true);
        settings.setDelimiterDetectionEnabled(true, '\t');
        CsvFormat format = new CsvFormat();
        format.setDelimiter(delimiter);
        settings.setFormat(format);
        settings.setReadInputOnSeparateThread(true);
        settings.setMaxCharsPerColumn(20000);
    }

    @Override
    public SourceIterator<Record> getSourceIterator() {
        return this::getItererableCsv;
    }


    private Iterable<Record> getItererableCsv(String source, LogicalSource logicalSource) {
        return new CsvParser(settings).iterateRecords(new StringReader(source));
    }
}
