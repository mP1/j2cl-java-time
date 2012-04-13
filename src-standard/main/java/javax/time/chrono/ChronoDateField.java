/*
 * Copyright (c) 2012, Stephen Colebourne & Michael Nascimento Santos
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  * Neither the name of JSR-310 nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package javax.time.chrono;

import static javax.time.builder.LocalDateUnit.DAYS;
import static javax.time.builder.LocalDateUnit.ERAS;
import static javax.time.builder.LocalDateUnit.FOREVER;
import static javax.time.builder.LocalDateUnit.MONTHS;
import static javax.time.builder.LocalDateUnit.WEEKS;
import static javax.time.builder.LocalDateUnit.YEARS;

import javax.time.CalendricalException;
import javax.time.DateTimes;
import javax.time.LocalDate;
import javax.time.LocalDateTime;
import javax.time.builder.CalendricalObject;
import javax.time.builder.DateField;
import javax.time.builder.LocalDateField;
import javax.time.builder.PeriodUnit;
import javax.time.calendrical.DateTimeRuleRange;

/**
 * The set of fields that can be accessed using a chronology.
 * <p>
 * These fields are the minimal set of fields supported by multiple calendar systems
 * as expressed via {@link Chrono}. The fields duplicate some of those in
 * {@link LocalDateField} however they are used in a different way.
 * The {@code LocalDateField} fields will always access the ISO calendar system,
 * whereas these fields will always access the supplied calendar system.
 * Only if no calendar system is specified will these fields default to ISO.
 * <p>
 * This is a final, immutable and thread-safe enum.
 */
public enum ChronoDateField implements DateField {

    DAY_OF_WEEK("ChronoDayOfWeek", DAYS, WEEKS),
    DAY_OF_MONTH("ChronoDayOfMonth", DAYS, MONTHS),
    DAY_OF_YEAR("ChronoDayOfYear", DAYS, YEARS),
    MONTH_OF_YEAR("ChronoMonthOfYear", MONTHS, YEARS),
    YEAR_OF_ERA("ChronoYearOfEra", YEARS, ERAS),
    PROLEPTIC_YEAR("ChronoProlepticYear", YEARS, FOREVER),
    ERA("ChronoEra", ERAS, FOREVER);

    private final String name;
    private final PeriodUnit baseUnit;
    private final PeriodUnit rangeUnit;

    private ChronoDateField(String name, PeriodUnit baseUnit, PeriodUnit rangeUnit) {
        this.name = name;
        this.baseUnit = baseUnit;
        this.rangeUnit = rangeUnit;
    }

    public String getName() {
        return name;
    }

    public PeriodUnit getBaseUnit() {
        return baseUnit;
    }

    public PeriodUnit getRangeUnit() {
        return rangeUnit;
    }

    @Override
    public long getValueFrom(CalendricalObject calendrical) {
        return 0;  // TODO
    }

//    public DateField bindTo(Chrono chrono) {
//        return new Rules(this, chrono);
//    }

    public Rules<ChronoDate> getRules() {
        return null;
    }

    @Override
    public DateTimeRuleRange getValueRange() {
        switch (this) {
            case DAY_OF_WEEK: return LocalDateField.DAY_OF_WEEK.getValueRange();
            case DAY_OF_MONTH: return LocalDateField.DAY_OF_MONTH.getValueRange();
            case DAY_OF_YEAR: return LocalDateField.DAY_OF_YEAR.getValueRange();
            case MONTH_OF_YEAR: return LocalDateField.MONTH_OF_YEAR.getValueRange();
            case YEAR_OF_ERA: return DateTimeRuleRange.of(1, DateTimes.MAX_YEAR);
            case PROLEPTIC_YEAR: return LocalDateField.YEAR.getValueRange();
            case ERA: return DateTimeRuleRange.of(0, 1);
        }
        throw new CalendricalException("Unknown field");
    }

    @Override
    public Rules<LocalDateTime> getDateTimeRules() {
        return null;
    }

    @Override
    public Rules<LocalDate> getDateRules() {
        return null;
    }

    @Override
    public String toString() {
        return getName();
    }

//    //-----------------------------------------------------------------------
//    /**
//     * Rules that convert a {@code ChronoField} to a {@code DateField}.
//     */
//    static class Rules implements DateField, DateTimeRules<LocalDate> {
//        private final ChronoField field;
//        private final Chrono chrono;
//        public Rules(ChronoField field, Chrono chrono) {
//            this.field = field;
//            this.chrono = chrono;
//        }
//        @Override
//        public String getName() {
//            return chrono.getName() + field.getName();
//        }
//        @Override
//        public PeriodUnit getBaseUnit() {
//            return field.getBaseUnit();
//        }
//        @Override
//        public PeriodUnit getRangeUnit() {
//            return field.getRangeUnit();
//        }
//        @Override
//        public DateTimeRuleRange getValueRange() {
//            return chrono.getRange(field);
//        }
//        @Override
//        public long getValueFrom(CalendricalObject calendrical) {
//            return getDateRules().get(calendrical.extract(LocalDate.class));
//        }
//        @Override
//        public DateTimeRules<LocalDateTime> getDateTimeRules() {
//            return new DateBasedDateTimeRules(this);
//        }
//        @Override
//        public DateTimeRules<LocalDate> getDateRules() {
//            return this;
//        }
//        @Override
//        public DateTimeRuleRange range(LocalDate date) {
//            return chrono.getRange(field);
//        }
//        @Override
//        public long get(LocalDate date) {
//            return chrono.getField(field, date.toEpochDay());
//        }
//        @Override
//        public LocalDate set(LocalDate date, long newValue) {
//            long updated = chrono.setField(field, date.toEpochDay(), (int) newValue);  // TODO cast
//            return LocalDate.ofEpochDay(updated);
//        }
//        @Override
//        public LocalDate setLenient(LocalDate date, long newValue) {
//            return null;  // TODO
//        }
//        @Override
//        public LocalDate roll(LocalDate date, long roll) {
//            return null;  // TODO
//        }
//    }

}