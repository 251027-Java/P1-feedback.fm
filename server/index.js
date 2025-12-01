import express from "express";
import cors from "cors";
import dotenv from "dotenv";
import pkg from "pg";

dotenv.config();
const { Pool } = pkg;

const app = express();
app.use(cors());
app.use(express.json());

// Postgres pool
const pool = new Pool({
  host: process.env.PGHOST,
  port: process.env.PGPORT,
  database: process.env.PGDATABASE,
  user: process.env.PGUSER,
  password: process.env.PGPASSWORD,
});

// test route
app.get("/api/health", async (req, res) => {
  try {
    const result = await pool.query("SELECT NOW()");
    res.json({ status: "ok", now: result.rows[0].now });
  } catch (err) {
    console.error(err);
    res.status(500).json({ error: "DB error" });
  }
});

// example: get songs
app.get("/api/songs", async (req, res) => {
  try {
    const result = await pool.query(`
      SELECT s.song_id, s.name, s.duration_ms, a.name AS artist_name
      FROM song s
      JOIN artist a ON s.artist_id = a.artist_id
      LIMIT 50;
    `);
    res.json(result.rows);
  } catch (err) {
    console.error(err);
    res.status(500).json({ error: "DB error" });
  }
});

const port = process.env.PORT || 4000;
app.listen(port, () => {
  console.log(`API server running on http://localhost:${port}`);
});
