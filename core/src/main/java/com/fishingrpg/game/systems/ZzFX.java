package com.fishingrpg.game.systems;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Random;

/**
 * Bộ Tổng Hợp Âm Thanh Thủ Tục Thời Gian Thực (ZzFX Procedural Sound Synthesizer).
 * Dựa trên thuật toán toán học ZzFX của Frank Force (KilledByAPixel).
 * 
 * - Tự động tổng hợp sóng âm thanh (Sine, Triangle, Sawtooth, Tan, Bitnoise, Noise) trực tiếp trong RAM.
 * - Tích hợp bộ chuyển đổi 16-bit PCM WAV (Little-Endian) và nạp thẳng vào OpenAL Sound Engine của LibGDX.
 * - Nặng 0 KB tệp tin bên ngoài, khởi tạo tức thì, giải quyết triệt để lỗi thiếu file âm thanh (FileNotFoundException).
 */
public class ZzFX {

    private static final String TAG = "ZzFX";
    private static final Random RNG = new Random();

    /**
     * Bảng tham số định nghĩa dạng sóng ZzFX.
     */
    public static class Parameters {
        public float volume = 1.0f;
        public float randomness = 0.05f;
        public float frequency = 220f;
        public float attack = 0f;
        public float sustain = 0f;
        public float release = 0.1f;
        public int shape = 0; // 0=Sine, 1=Triangle, 2=Sawtooth, 3=Tan, 4=Bitnoise, 5=Noise
        public float shapeCurve = 1.0f;
        public float slide = 0f;
        public float deltaSlide = 0f;
        public float pitchJump = 0f;
        public float pitchJumpTime = 0f;
        public float repeatTime = 0f;
        public float noise = 0f;
        public float modulation = 0f;
        public float bitCrush = 0f;
        public float delay = 0f;
        public float sustainVolume = 1.0f;
        public float decay = 0f;
        public float tremolo = 0f;
        public int sampleRate = 44100;

        public Parameters copy() {
            Parameters p = new Parameters();
            p.volume = this.volume;
            p.randomness = this.randomness;
            p.frequency = this.frequency;
            p.attack = this.attack;
            p.sustain = this.sustain;
            p.release = this.release;
            p.shape = this.shape;
            p.shapeCurve = this.shapeCurve;
            p.slide = this.slide;
            p.deltaSlide = this.deltaSlide;
            p.pitchJump = this.pitchJump;
            p.pitchJumpTime = this.pitchJumpTime;
            p.repeatTime = this.repeatTime;
            p.noise = this.noise;
            p.modulation = this.modulation;
            p.bitCrush = this.bitCrush;
            p.delay = this.delay;
            p.sustainVolume = this.sustainVolume;
            p.decay = this.decay;
            p.tremolo = this.tremolo;
            p.sampleRate = this.sampleRate;
            return p;
        }
    }

    /**
     * Danh mục âm thanh thủ tục thiết kế sẵn (Presets).
     */
    public enum Preset {
        BUTTON_CLICK, // Click phím cơ học, menu, nút bấm
        SUCCESS,      // Bắt cá thành công, hoàn thành nhiệm vụ
        FAIL,         // Cá thoát, hết lượt, thao tác lỗi
        CAST,         // Quăng cần vút dây
        BITE,         // Cá đớp mồi (snappy chomp)
        PULL,         // Giằng co, siết cước kéo cá
        SPLASH,       // Nước bắn tóe, mồi chạm mặt nước
        COIN,         // Nhận vàng, ngọc, kim cương leng keng
        SKILL,        // Tung kỹ năng phép thuật bùng nổ
        LEVEL_UP,     // Thăng cấp nhân vật, mở khóa bản đồ
        REEL_CLICK,   // Bánh răng máy câu quay rè rè
        ALERT,        // Báo động cá boss, cảnh báo dây sắp đứt
        EXPLOSION     // Tiếng nổ trầm sâu, rồng biển gầm
    }

    /**
     * Lấy cấu hình tham số cho từng Preset mẫu.
     */
    public static Parameters getPresetParameters(Preset preset) {
        Parameters p = new Parameters();
        switch (preset) {
            case BUTTON_CLICK:
                // Click nhẹ, dứt khoát, âm sắc sắc nét
                p.volume = 0.8f;
                p.frequency = 440f;
                p.attack = 0.005f;
                p.decay = 0.01f;
                p.sustain = 0f;
                p.release = 0.035f;
                p.shape = 1; // Triangle
                p.slide = -4f;
                p.randomness = 0.02f;
                break;

            case SUCCESS:
                // Hợp âm thăng hoa 2 nhịp vút lên
                p.volume = 1.0f;
                p.frequency = 523.25f; // Đô 5
                p.attack = 0.01f;
                p.decay = 0.05f;
                p.sustain = 0.12f;
                p.release = 0.28f;
                p.shape = 0; // Sine mượt
                p.pitchJump = 12f;
                p.pitchJumpTime = 0.08f;
                p.slide = 2f;
                p.sustainVolume = 0.85f;
                break;

            case FAIL:
                // Tiếng trượt trầm thể hiện hụt hẫng
                p.volume = 0.9f;
                p.frequency = 280f;
                p.attack = 0.01f;
                p.decay = 0.08f;
                p.sustain = 0.08f;
                p.release = 0.32f;
                p.shape = 2; // Sawtooth
                p.slide = -14f;
                p.deltaSlide = -2f;
                break;

            case CAST:
                // Tiếng vút dây câu trong không khí (Whoosh)
                p.volume = 0.85f;
                p.frequency = 180f;
                p.attack = 0.02f;
                p.decay = 0.06f;
                p.sustain = 0.04f;
                p.release = 0.25f;
                p.shape = 5; // Noise kết hợp slide
                p.noise = 0.65f;
                p.slide = 12f;
                break;

            case BITE:
                // Tiếng cá đớp phao đánh bõm
                p.volume = 1.0f;
                p.frequency = 260f;
                p.attack = 0.005f;
                p.decay = 0.03f;
                p.sustain = 0.02f;
                p.release = 0.12f;
                p.shape = 3; // Tan (độ nảy cao)
                p.slide = -18f;
                p.noise = 0.25f;
                break;

            case PULL:
                // Cước căng giằng co với cá
                p.volume = 0.8f;
                p.frequency = 150f;
                p.attack = 0.02f;
                p.decay = 0.05f;
                p.sustain = 0.08f;
                p.release = 0.18f;
                p.shape = 2; // Sawtooth
                p.modulation = 30f;
                p.tremolo = 0.4f;
                break;

            case SPLASH:
                // Nước bắn tóe khi cá quẫy
                p.volume = 0.95f;
                p.frequency = 120f;
                p.attack = 0.01f;
                p.decay = 0.08f;
                p.sustain = 0.05f;
                p.release = 0.35f;
                p.shape = 5; // White noise
                p.noise = 0.9f;
                p.slide = -6f;
                break;

            case COIN:
                // Tiếng kim cương / vàng leng keng
                p.volume = 0.9f;
                p.frequency = 987.77f; // Si 5
                p.attack = 0.005f;
                p.decay = 0.03f;
                p.sustain = 0.1f;
                p.release = 0.38f;
                p.shape = 0; // Sine
                p.pitchJump = 18f;
                p.pitchJumpTime = 0.05f;
                p.sustainVolume = 0.8f;
                break;

            case SKILL:
                // Phóng kỹ năng ma thuật rực rỡ
                p.volume = 1.0f;
                p.frequency = 580f;
                p.attack = 0.01f;
                p.decay = 0.06f;
                p.sustain = 0.15f;
                p.release = 0.42f;
                p.shape = 4; // Bitnoise / FM
                p.slide = -8f;
                p.modulation = 45f;
                p.tremolo = 0.25f;
                break;

            case LEVEL_UP:
                // Chuỗi nốt chiến thắng thăng cấp
                p.volume = 1.0f;
                p.frequency = 440f;
                p.attack = 0.01f;
                p.decay = 0.08f;
                p.sustain = 0.25f;
                p.release = 0.55f;
                p.shape = 0;
                p.pitchJump = 24f;
                p.pitchJumpTime = 0.12f;
                p.slide = 4f;
                break;

            case REEL_CLICK:
                // Tiếng lách cách bánh răng máy câu
                p.volume = 0.7f;
                p.frequency = 750f;
                p.attack = 0.002f;
                p.decay = 0.008f;
                p.sustain = 0f;
                p.release = 0.02f;
                p.shape = 1; // Triangle
                p.bitCrush = 0.15f;
                break;

            case ALERT:
                // Bíp báo động hai nhịp dồn dập
                p.volume = 0.95f;
                p.frequency = 700f;
                p.attack = 0.01f;
                p.decay = 0.04f;
                p.sustain = 0.06f;
                p.release = 0.22f;
                p.shape = 1;
                p.pitchJump = -12f;
                p.pitchJumpTime = 0.06f;
                break;

            case EXPLOSION:
                // Nổ rền vang, sấm sét
                p.volume = 1.0f;
                p.frequency = 65f;
                p.attack = 0.02f;
                p.decay = 0.15f;
                p.sustain = 0.18f;
                p.release = 0.65f;
                p.shape = 5;
                p.noise = 1.0f;
                p.slide = -4f;
                break;
        }
        return p;
    }

    /**
     * Thuật toán toán học sinh mảng float samples theo chuẩn ZzFX.
     */
    public static float[] generateSamples(Parameters p) {
        int sampleRate = p.sampleRate > 0 ? p.sampleRate : 44100;
        double pi2 = Math.PI * 2.0;

        float startSlide = (float) (p.slide * 500.0 * pi2 / sampleRate / sampleRate);
        float currentSlide = startSlide;

        float randFactor = (1.0f + (RNG.nextFloat() * 2.0f - 1.0f) * p.randomness);
        float startFreq = (float) (p.frequency * randFactor * pi2 / sampleRate);
        float currentFreq = startFreq;

        int attackSamples = (int) (p.attack * sampleRate);
        int decaySamples = (int) (p.decay * sampleRate);
        int sustainSamples = (int) (p.sustain * sampleRate);
        int releaseSamples = (int) (p.release * sampleRate);
        int delaySamples = (int) (p.delay * sampleRate);

        int totalSamples = attackSamples + decaySamples + sustainSamples + releaseSamples + delaySamples;
        if (totalSamples <= 0) totalSamples = (int) (0.05f * sampleRate);

        float[] b = new float[totalSamples];

        double deltaSlideVal = p.deltaSlide * 500.0 * pi2 / Math.pow(sampleRate, 3);
        double modulationVal = p.modulation * pi2 / sampleRate;
        double pitchJumpVal = p.pitchJump * pi2 / sampleRate;
        int pitchJumpSamples = (int) (p.pitchJumpTime * sampleRate);
        int repeatSamples = (int) (p.repeatTime * sampleRate);

        double t = 0.0;
        double tm = 0.0;
        int j = 1;
        int r = 0;
        int c = 0;
        float s = 0f;

        int crushStep = (int) (p.bitCrush * 100.0f);
        if (crushStep < 1) crushStep = 1;

        for (int i = 0; i < totalSamples; i++) {
            if ((++c % crushStep) == 0) {
                // 1. Tính toán giá trị sóng cơ bản theo shape
                switch (p.shape) {
                    case 1: // Triangle
                        s = (float) (1.0 - 4.0 * Math.abs(Math.round(t / pi2) - t / pi2));
                        break;
                    case 2: // Sawtooth
                        s = (float) (1.0 - ((2.0 * t / pi2) % 2.0 + 2.0) % 2.0);
                        break;
                    case 3: // Tan
                        s = (float) Math.max(Math.min(Math.tan(t), 1.0), -1.0);
                        break;
                    case 4: // Bitnoise / Cubic Sine
                        s = (float) Math.sin(Math.pow(t % pi2, 3.0));
                        break;
                    case 5: // White noise
                        s = (RNG.nextFloat() * 2.0f - 1.0f);
                        break;
                    case 0: // Sine
                    default:
                        s = (float) Math.sin(t);
                        break;
                }

                // 2. Shape Curve & Tremolo
                float sign = (s >= 0f) ? 1.0f : -1.0f;
                float trem = (repeatSamples > 0) ? (float) (1.0 - p.tremolo + p.tremolo * Math.sin(pi2 * i / repeatSamples)) : 1.0f;
                s = trem * sign * (float) Math.pow(Math.abs(s), p.shapeCurve);

                // 3. Attack - Decay - Sustain - Release Envelope
                float env = 0f;
                if (i < attackSamples && attackSamples > 0) {
                    env = (float) i / attackSamples;
                } else if (i < attackSamples + decaySamples && decaySamples > 0) {
                    env = 1.0f - ((float) (i - attackSamples) / decaySamples) * (1.0f - p.sustainVolume);
                } else if (i < attackSamples + decaySamples + sustainSamples) {
                    env = p.sustainVolume;
                } else if (i < totalSamples - delaySamples && releaseSamples > 0) {
                    env = (1.0f - (float) (i - attackSamples - decaySamples - sustainSamples) / releaseSamples) * p.sustainVolume;
                }
                s *= Math.max(0f, env);

                // 4. Echo / Delay Feedback
                if (delaySamples > 0) {
                    float delayed = 0f;
                    if (i > delaySamples) {
                        float prev = b[i - delaySamples];
                        delayed = (prev >= 0 ? 1f : -1f) * (prev * prev) * 0.5f;
                    }
                    s = s * 0.5f + delayed;
                }

                // 5. Thêm Noise nếu có cấu hình
                if (p.noise > 0f) {
                    s += (RNG.nextFloat() * 2.0f - 1.0f) * p.noise;
                }
            }

            // 6. Tích lũy Phase và Tần số
            currentSlide += deltaSlideVal;
            currentFreq += currentSlide;
            double f = currentFreq * Math.sin(tm);
            t += f - f * modulationVal;
            tm += modulationVal;

            // 7. Nhảy Pitch (Pitch Jump)
            if (pitchJumpSamples > 0 && ++j > pitchJumpSamples) {
                currentFreq += pitchJumpVal;
                startFreq += pitchJumpVal;
                j = 0;
            }

            // 8. Lặp chu kỳ (Repeat Time)
            if (repeatSamples > 0 && !(++r % repeatSamples != 0)) {
                currentFreq = startFreq;
                currentSlide = startSlide;
                j = 1;
            }

            // Lưu mẫu, nhân âm lượng và kẹp ngưỡng chống vỡ tiếng
            float sampleOut = s * p.volume;
            if (sampleOut > 1.0f) sampleOut = 1.0f;
            if (sampleOut < -1.0f) sampleOut = -1.0f;
            b[i] = sampleOut;
        }

        return b;
    }

    /**
     * Xuất mảng float samples ra dạng mảng byte chuẩn file WAV 16-bit Mono (Little-Endian).
     */
    public static byte[] generateWav(Parameters p) {
        float[] samples = generateSamples(p);
        int sampleRate = p.sampleRate > 0 ? p.sampleRate : 44100;
        int numSamples = samples.length;
        int dataSize = numSamples * 2; // 16-bit = 2 bytes / sample
        int totalSize = 44 + dataSize;

        ByteBuffer buffer = ByteBuffer.allocate(totalSize);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        // RIFF Header
        buffer.put((byte) 'R').put((byte) 'I').put((byte) 'F').put((byte) 'F');
        buffer.putInt(36 + dataSize); // ChunkSize
        buffer.put((byte) 'W').put((byte) 'A').put((byte) 'V').put((byte) 'E');

        // fmt subchunk
        buffer.put((byte) 'f').put((byte) 'm').put((byte) 't').put((byte) ' ');
        buffer.putInt(16);          // Subchunk1Size (16 for PCM)
        buffer.putShort((short) 1); // AudioFormat (1 = PCM)
        buffer.putShort((short) 1); // NumChannels (1 = Mono)
        buffer.putInt(sampleRate);  // SampleRate
        buffer.putInt(sampleRate * 2); // ByteRate (SampleRate * Channels * Bits/8)
        buffer.putShort((short) 2); // BlockAlign (Channels * Bits/8)
        buffer.putShort((short) 16); // BitsPerSample

        // data subchunk
        buffer.put((byte) 'd').put((byte) 'a').put((byte) 't').put((byte) 'a');
        buffer.putInt(dataSize);

        // Ghi dữ liệu 16-bit signed PCM
        for (float s : samples) {
            short val = (short) Math.round(s * 32767.0f);
            buffer.putShort(val);
        }

        return buffer.array();
    }

    /**
     * Tạo một đối tượng LibGDX Sound hoàn chỉnh trong bộ nhớ RAM từ bộ tham số ZzFX.
     */
    public static Sound createSound(Parameters p) {
        if (Gdx.audio == null) {
            Gdx.app.log(TAG, "Gdx.audio is null, cannot create procedural sound.");
            return null;
        }

        try {
            byte[] wavBytes = generateWav(p);
            MemoryFileHandle handle = new MemoryFileHandle("zzfx_sound.wav", wavBytes);
            return Gdx.audio.newSound(handle);
        } catch (Exception e) {
            Gdx.app.error(TAG, "Lỗi khởi tạo âm thanh thủ tục ZzFX", e);
            return null;
        }
    }

    /**
     * Tạo đối tượng Sound trực tiếp từ một Preset có sẵn.
     */
    public static Sound createPresetSound(Preset preset) {
        return createSound(getPresetParameters(preset));
    }

    /**
     * Tự động ánh xạ tên file SFX (ví dụ "sfx_button", "sfx_cast") sang Preset tương ứng.
     */
    public static Sound createSoundByName(String sfxName) {
        if (sfxName == null) return null;
        String name = sfxName.toLowerCase().trim();

        if (name.contains("button") || name.contains("click")) {
            return createPresetSound(Preset.BUTTON_CLICK);
        } else if (name.contains("success") || name.contains("victory") || name.contains("win")) {
            return createPresetSound(Preset.SUCCESS);
        } else if (name.contains("fail") || name.contains("escape") || name.contains("lose")) {
            return createPresetSound(Preset.FAIL);
        } else if (name.contains("cast") || name.contains("throw")) {
            return createPresetSound(Preset.CAST);
        } else if (name.contains("bite") || name.contains("nibble") || name.contains("catch")) {
            return createPresetSound(Preset.BITE);
        } else if (name.contains("pull") || name.contains("tug") || name.contains("tension")) {
            return createPresetSound(Preset.PULL);
        } else if (name.contains("splash") || name.contains("water")) {
            return createPresetSound(Preset.SPLASH);
        } else if (name.contains("coin") || name.contains("gold") || name.contains("diamond") || name.contains("money")) {
            return createPresetSound(Preset.COIN);
        } else if (name.contains("skill") || name.contains("magic") || name.contains("spell")) {
            return createPresetSound(Preset.SKILL);
        } else if (name.contains("level") || name.contains("rank") || name.contains("star")) {
            return createPresetSound(Preset.LEVEL_UP);
        } else if (name.contains("reel") || name.contains("crank")) {
            return createPresetSound(Preset.REEL_CLICK);
        } else if (name.contains("alert") || name.contains("warning") || name.contains("ping")) {
            return createPresetSound(Preset.ALERT);
        } else if (name.contains("explosion") || name.contains("boom") || name.contains("boss")) {
            return createPresetSound(Preset.EXPLOSION);
        }

        // Mặc định tạo tiếng click nhẹ nếu không trùng khớp tên
        return createPresetSound(Preset.BUTTON_CLICK);
    }

    /**
     * FileHandle ảo chạy trên bộ nhớ RAM không cần lưu file vật lý trên đĩa cứng.
     */
    public static class MemoryFileHandle extends FileHandle {
        private final byte[] data;
        private final String fileName;

        public MemoryFileHandle(String fileName, byte[] data) {
            super(fileName, FileType.Internal);
            this.fileName = fileName;
            this.data = data;
        }

        @Override
        public InputStream read() {
            return new ByteArrayInputStream(data);
        }

        @Override
        public long length() {
            return data.length;
        }

        @Override
        public boolean exists() {
            return true;
        }

        @Override
        public String extension() {
            return "wav";
        }

        @Override
        public String name() {
            return fileName;
        }
    }
}
