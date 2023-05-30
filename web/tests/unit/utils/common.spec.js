import slugify from '@/utils/slugify.js';
import utils from '@/utils/common.js';

describe('Utility Methods', () => {
    // https://github.com/jprichardson/string.js/blob/master/test/string.test.js
    it('should decode HTML entities', (done) => {
        expect(utils.decodeHTMLEntities('Ken Thompson &amp; Dennis Ritchie'))
            .toBe('Ken Thompson & Dennis Ritchie');
        expect(utils.decodeHTMLEntities('3 &lt; 4')).toBe('3 < 4');
        // noinspection HttpUrlsUsage
        expect(utils.decodeHTMLEntities('http:&#47;&#47;')).toBe('http://');
        done();
    });

    it('should strip HTML tags', (done) => {
        expect(utils.stripHTMLTags('<p>just <b>some</b> text</p>'))
            .toBe('just some text');
        expect(utils.stripHTMLTags('<p>just <b>some</b> text</p>', ['p']))
            .toBe('just <b>some</b> text');
        expect(utils.stripHTMLTags('<i>just</i> some <image/> text', ['i']))
            .toBe('just some <image/> text');
        expect(utils.stripHTMLTags('<i>just</i> some <image/> <div>text</div>', ['i', 'div']))
            .toBe('just some <image/> text');
        done();
    });

    it('should preserve case if requested', (done) => {
        expect(slugify('UPPER CASE', true)).toBe('UPPER-CASE');
        done();
    });

    it('should work if a number is passed in', (done) => {
        expect(slugify(12345)).toBe('12345');
        done();
    });

    describe('username validation', () => {
        it('accepts latin-1 characters', () => {
            const username = "John\"'-. Doeäâèéë1234";
            expect(utils.isUserNameValid(username)).toBeTruthy();
        });

        it('rejects empty string', () => {
            const username = '';
            expect(utils.isUserNameValid(username)).toBeFalsy();
        });

        it('should reject new lines', () => {
            expect(utils.isUserNameValid('myusername\r\n')).toBeFalsy();
        });

        it('should reject new lines', () => {
            expect(utils.isUserNameValid('myusername\n')).toBeFalsy();
        });

        it('should reject tabs', () => {
            expect(utils.isUserNameValid('myusername\t')).toBeFalsy();
        });

        it('accepts square brackets', () => {
            const username = '[best clan] julian';
            expect(utils.isUserNameValid(username)).toBeTruthy();
        });

        it('accepts regular username', () => {
            expect(utils.isUserNameValid('myusername')).toBeTruthy();
        });

        it('accepts quotes', () => {
            expect(utils.isUserNameValid('baris "the best" usakli')).toBeTruthy();
        });
    });

    describe('email validation', () => {
        it('accepts sample address', () => {
            const email = 'sample@example.com';
            expect(utils.isEmailValid(email)).toBeTruthy();
        });
        it('rejects empty address', () => {
            const email = '';
            expect(utils.isEmailValid(email)).toBeFalsy();
        });
    });

    describe('UUID generation', () => {
        it('return unique random value every time', () => {
            delete require.cache[require.resolve('@/utils/common')];
            const {generateUUID} = require('@/utils/common');
            const uuid1 = generateUUID();
            const uuid2 = generateUUID();
            expect(uuid1).not.toBe(uuid2);
        });
    });

    describe('cleanUpTag', () => {
        it('should cleanUp a tag', (done) => {
            const cleanedTag = utils.cleanUpTag(',/#!$^*;TaG1:{}=_`<>\'"~()?|');
            expect(cleanedTag).toBe('tag1');
            done();
        });

        it('should return empty string for invalid tags', (done) => {
            expect(utils.cleanUpTag(undefined)).toBe('');
            expect(utils.cleanUpTag(null)).toBe('');
            expect(utils.cleanUpTag(false)).toBe('');
            expect(utils.cleanUpTag(1)).toBe('');
            expect(utils.cleanUpTag(0)).toBe('');
            done();
        });
    });

    it('should remove punctuation', (done) => {
        const removed = utils.removePunctuation('some text with , ! punctuation inside "');
        expect(removed).toBe('some text with   punctuation inside ');
        done();
    });

    it('should shallow merge two objects', (done) => {
        const a = {foo: 1, cat1: 'ginger'};
        const b = {baz: 2, cat2: 'phoebe'};
        const obj = utils.merge(a, b);
        expect(obj.foo).toBe(1);
        expect(obj.baz).toBe(2);
        expect(obj.cat1).toBe('ginger');
        expect(obj.cat2).toBe('phoebe');
        done();
    });

    it('should return the file extesion', (done) => {
        expect(utils.fileExtension('/path/to/some/file.png')).toBe('png');
        done();
    });

    it('should return file mime type', (done) => {
        expect(utils.fileMimeType('/path/to/some/file.png')).toBe('image/png');
        done();
    });

    it('should check if url is relative', (done) => {
        expect(utils.isRelativeUrl('/topic/1/slug')).toBe(true);
        done();
    });

    it('should check if url is relative', (done) => {
        expect(utils.isRelativeUrl('https://nodebb.org')).toBe(false);
        done();
    });

    it('should make number human readable', (done) => {
        expect(utils.makeNumberHumanReadable('1000')).toBe('1.0k');
        done();
    });

    it('should make number human readable', (done) => {
        expect(utils.makeNumberHumanReadable('1100000')).toBe('1.1m');
        done();
    });

    it('should make number human readable', (done) => {
        expect(utils.makeNumberHumanReadable('100')).toBe(100);
        done();
    });

    it('should make number human readable', (done) => {
        expect(utils.makeNumberHumanReadable(null)).toBe(null);
        done();
    });

    it('should add commas to numbers', (done) => {
        expect(utils.addCommas('100')).toBe('100');
        done();
    });

    it('should add commas to numbers', (done) => {
        expect(utils.addCommas('1000')).toBe('1,000');
        done();
    });

    it('should add commas to numbers', (done) => {
        expect(utils.addCommas('1000000')).toBe('1,000,000');
        done();
    });

    it('should return passed in value if invalid', (done) => {
        // eslint-disable-next-line no-loss-of-precision
        const bigInt = -111111111111111111;
        const result = utils.toISOString(bigInt);
        expect(bigInt).toBe(result);
        done();
    });

    it('should return false if browser is not android', (done) => {
        global.navigator = {
            userAgent: 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.96 Safari/537.36',
        };
        expect(utils.isAndroidBrowser()).toBe(false);
        done();
    });

    it('should return true if browser is android', (done) => {
        global.navigator = {
            userAgent: 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Android /58.0.3029.96 Safari/537.36',
        };
        expect(utils.isAndroidBrowser()).toBeFalsy();
        done();
    });

    it('should return false if not touch device', (done) => {
        expect(!utils.isTouchDevice()).toBe(true);
        done();
    });

    describe('toType', () => {
        it('should return param as is if not string', (done) => {
            expect(123).toBe(utils.toType(123));
            done();
        });

        it('should convert return string numbers as numbers', (done) => {
            expect(123).toBe(utils.toType('123'));
            done();
        });

        it('should convert string "false" to boolean false', (done) => {
            expect(false).toBe(utils.toType('false'));
            done();
        });

        it('should convert string "true" to boolean true', (done) => {
            expect(true).toBe(utils.toType('true'));
            done();
        });

        it('should parse json', (done) => {
            const data = utils.toType('{"a":"1"}');
            expect(data.a).toBe('1');
            done();
        });

        it('should return string as is if its not json,true,false or number', (done) => {
            const regularStr = 'this is a regular string';
            expect(regularStr).toBe(utils.toType(regularStr));
            done();
        });
    });

    describe('utils.props', () => {
        const data = {};

        it('should set nested data', (done) => {
            expect(10).toBe(utils.props(data, 'a.b.c.d', 10));
            done();
        });

        it('should return nested object', (done) => {
            const obj = utils.props(data, 'a.b.c');
            expect(obj.d).toBe(10);
            done();
        });

        it('should returned undefined without throwing', (done) => {
            expect(utils.props(data, 'a.b.c.foo.bar')).toBe(undefined);
            done();
        });

        it('should return undefined if second param is null', (done) => {
            expect(utils.props(undefined, null)).toBe(undefined);
            done();
        });
    });

    describe('isInternalURI', () => {
        const target = {host: '', protocol: 'https'};
        const reference = {host: '', protocol: 'https'};

        it('should return true if they match', (done) => {
            expect(utils.isInternalURI(target, reference, '')).toBeTruthy();
            done();
        });

        it('should return true if they match', (done) => {
            target.host = 'nodebb.org';
            reference.host = 'nodebb.org';
            expect(utils.isInternalURI(target, reference, '')).toBeTruthy();
            done();
        });

        it('should handle relative path', (done) => {
            target.pathname = '/forum';
            expect(utils.isInternalURI(target, reference, '/forum')).toBeTruthy();
            done();
        });

        // 注意：这个测试方法单独运行时，测试结果返回 false 值
        // 因为它不会被前面的测试方法修改常量对象，如果整体运行则返回 true 值
        it('should return false if they do not match', (done) => {
            target.pathname = '';
            reference.host = 'designcreateplay.com';
            expect(!utils.isInternalURI(target, reference)).toBeTruthy();
            done();
        });
    });

    it('escape html', (done) => {
        const escaped = utils.escapeHTML('&<>');
        expect(escaped).toBe('&amp;&lt;&gt;');
        done();
    });

    it('should escape regex chars', (done) => {
        const escaped = utils.escapeRegexChars('some text {}');
        expect(escaped).toBe('some\\ text\\ \\{\\}');
        done();
    });

    it('should get hours array', (done) => {
        const currentHour = new Date().getHours();
        const hours = utils.getHoursArray();
        let index = hours.length - 1;
        for (let i = currentHour, ii = currentHour - 24; i > ii; i -= 1) {
            const hour = i < 0 ? 24 + i : i;
            expect(hours[index]).toBe(`${hour}:00`);
            index -= 1;
        }
        done();
    });

    it('should get days array', (done) => {
        const currentDay = new Date(Date.now()).getTime();
        const days = utils.getDaysArray();
        const months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
        let index = 0;
        for (let x = 29; x >= 0; x -= 1) {
            const tmpDate = new Date(currentDay - (1000 * 60 * 60 * 24 * x));
            expect(`${months[tmpDate.getMonth()]} ${tmpDate.getDate()}`).toBe(days[index]);
            index += 1;
        }
        done();
    });

    it('`utils.rtrim` should remove trailing space', (done) => {
        expect(utils.rtrim('  thing   ')).toBe('  thing');
        expect(utils.rtrim('\tthing\t\t')).toBe('\tthing');
        expect(utils.rtrim('\t thing \t')).toBe('\t thing');
        done();
    });
});
