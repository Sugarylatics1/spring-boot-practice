import { useState, useEffect } from 'react'

function App() {
    const [metrics, setMetrics] = useState({
        requests_total: 0,
        rate_limit_hits: 0,
        request_latency_p95_us: 0,
        active_ips: 0
    })
    const [loading, setLoading] = useState(true)
    const [error, setError] = useState(null)

    const fetchMetrics = async () => {
        try {
            const response = await fetch('/metrics')
            if (!response.ok) {
                throw new Error(`HTTP ${response.status}: ${response.statusText}`)
            }

            const text = await response.text()
            console.log('Raw metrics:', text) // Debug log

            // Parse Prometheus format more robustly
            const lines = text.split('\n')
            const parsed = {}

            lines.forEach(line => {
                // Skip comments and empty lines
                if (line.startsWith('#') || !line.trim()) return

                // Split on last space (metric name and value)
                const lastSpace = line.lastIndexOf(' ')
                if (lastSpace === -1) return

                const key = line.substring(0, lastSpace).trim()
                const value = parseFloat(line.substring(lastSpace + 1).trim())

                if (!isNaN(value)) {
                    parsed[key] = value
                }
            })

            console.log('Parsed metrics:', parsed) // Debug log
            setMetrics(parsed)
            setLoading(false)
        } catch (err) {
            console.error('Metrics fetch error:', err)
            setError(err.message)
            setLoading(false)
        }
    }

    useEffect(() => {
        fetchMetrics()
        // Auto-refresh every 5 seconds
        const interval = setInterval(fetchMetrics, 5000)
        return () => clearInterval(interval)
    }, [])

    const testPing = async () => {
        try {
            await fetch('/ping', {
                headers: { 'Authorization': 'nexus_7fa94376-75b4-4bf8-80d1-6a01e455ffd0' }
            })
            // Refresh metrics after ping
            setTimeout(fetchMetrics, 500)
        } catch (err) {
            console.error('Ping error:', err)
        }
    }

    if (loading) return <div>Loading metrics...</div>
    if (error) return <div>Error: {error}</div>

    return (
        <div style={{ fontFamily: 'sans-serif', padding: '2rem', maxWidth: '1200px', margin: '0 auto' }}>
            <h1 style={{ textAlign: 'center', marginBottom: '2rem' }}>API Gateway Dashboard</h1>

            <div style={{ display: 'grid', gap: '1.5rem', gridTemplateColumns: 'repeat(auto-fit, minmax(250px, 1fr))' }}>
                <div style={{
                    border: '2px solid #e0e0e0',
                    padding: '1.5rem',
                    borderRadius: '12px',
                    textAlign: 'center',
                    background: '#fafafa'
                }}>
                    <h3 style={{ margin: '0 0 1rem 0', color: '#666' }}>Total Requests</h3>
                    <p style={{ fontSize: '3rem', margin: 0, fontWeight: 'bold', color: '#1976d2' }}>
                        {metrics['requests_total']?.toLocaleString() || 0}
                    </p>
                </div>

                <div style={{
                    border: '2px solid #e0e0e0',
                    padding: '1.5rem',
                    borderRadius: '12px',
                    textAlign: 'center',
                    background: '#fafafa'
                }}>
                    <h3 style={{ margin: '0 0 1rem 0', color: '#666' }}>Rate Limited</h3>
                    <p style={{ fontSize: '3rem', margin: 0, fontWeight: 'bold', color: '#d32f2f' }}>
                        {metrics['rate_limit_hits']?.toLocaleString() || 0}
                    </p>
                </div>

                <div style={{
                    border: '2px solid #e0e0e0',
                    padding: '1.5rem',
                    borderRadius: '12px',
                    textAlign: 'center',
                    background: '#fafafa'
                }}>
                    <h3 style={{ margin: '0 0 1rem 0', color: '#666' }}>P95 Latency</h3>
                    <p style={{ fontSize: '3rem', margin: 0, fontWeight: 'bold', color: '#388e3c' }}>
                        {metrics['request_latency_p95_us'] ?
                            `${(metrics['request_latency_p95_us'] / 1000).toFixed(2)} ms` : '0.00 ms'}
                    </p>
                </div>

                <div style={{
                    border: '2px solid #e0e0e0',
                    padding: '1.5rem',
                    borderRadius: '12px',
                    textAlign: 'center',
                    background: '#fafafa'
                }}>
                    <h3 style={{ margin: '0 0 1rem 0', color: '#666' }}>Active IPs</h3>
                    <p style={{ fontSize: '3rem', margin: 0, fontWeight: 'bold', color: '#f57c00' }}>
                        {metrics['active_ips'] || 0}
                    </p>
                </div>
            </div>

            <div style={{ textAlign: 'center', marginTop: '3rem' }}>
                <button
                    onClick={testPing}
                    style={{
                        padding: '0.75rem 2rem',
                        background: '#1976d2',
                        color: 'white',
                        border: 'none',
                        borderRadius: '8px',
                        cursor: 'pointer',
                        fontSize: '1rem',
                        fontWeight: 'bold'
                    }}
                >
                    Test /ping Endpoint
                </button>
                <p style={{ color: '#666', marginTop: '1rem', fontSize: '0.9rem' }}>
                    Click to generate traffic (requires auth: secret123)
                </p>
            </div>
        </div>
    )
}

export default App